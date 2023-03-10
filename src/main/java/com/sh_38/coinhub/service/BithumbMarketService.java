package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.dto.CoinSellDTO;
import com.sh_38.coinhub.feign.BithumbFeignClient;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BithumbMarketService implements MarketService {
    private final BithumbFeignClient bithumbFeignClient;

    @Value("${feeUrl.bithumb}")
    private String feeUrl;


    public double getCoinCurrentPrice(String coin) {
        return Double.parseDouble(
                bithumbFeignClient.getCoinPrice(coin.toUpperCase()+"_KRW")
                        .getData()
                        .getClosing_price());

    }

    public List<String> getCoins() {
        List<String> result = new ArrayList<>();
        bithumbFeignClient.getAssetStatus().getData().forEach((k,v) -> {
            if(v.getDeposit_status()==1 && v.getWithdrawal_status()==1) {
                result.add(k.toUpperCase());
            }

        });
        return result;
    }

    public CoinBuyDTO calculateBuy(List<String> commonCoins, double amount) {
        Map<String, Double> amounts = new HashMap<>();
        // Buy 할때는 가격이 싼 코인부터 구매해서
        Map<String, SortedMap<Double, Double>> orderBooks = new HashMap<>();

        // Feign으로 orderbook 가져오기
        Map<String, Object> bithumbResponse = bithumbFeignClient.getOrderBook().getData();

        // orderbook for 돌면
        bithumbResponse.forEach((k, v) -> { // key: coin, v: object
            if(!(k.equalsIgnoreCase("timestamp") || k.equalsIgnoreCase("payment_currency"))) {
                double availableCurrency = amount;
                double availableCoin = 0;

                String coin = k;
                SortedMap<Double, Double> eachOrderBook = new TreeMap<>();
                List<Map<String, String>> wannaSell =
                        (List<Map<String, String>>)((Map<String, Object>) v).get("asks");

                // sort 메소드는 Comparator 가 필요하다 (비교연산자 오버라이딩), 오름차순
                wannaSell.sort( (k1, k2) -> {
                    if ( Double.parseDouble(k1.get("price")) > Double.parseDouble(k2.get("price"))) return 1;
                    return -1;
                });

                //wannaSell.sort(Comparator.comparingDouble(k1 -> Double.parseDouble(k1.get("price"))));



                for(int i=0; i<wannaSell.size(); i++) {
                    Double price = Double.parseDouble(wannaSell.get(i).get("price"));
                    Double quantity = Double.parseDouble(wannaSell.get(i).get("quantity"));
                    Double eachTotalPrice = price * quantity;
                    Double buyableCoinAmount = availableCurrency / price;

                    // 해당 호가창의 총 가격보다 큰지 작은지 비교

                    // amount <= X: 현재 호가창에서 내가 살수있는만큼 추가하고 종료
                    if(availableCurrency <= eachTotalPrice) {
                        availableCoin += buyableCoinAmount;
                        // 살수있는 호가창에 추가
                        eachOrderBook.put(price, buyableCoinAmount);
                        availableCurrency = 0;
                        break;
                    } else { // amount > X : 다음 스텝으로 넘어가고, 현재 호가창보다 내가 돈이 더 많은경우
                        availableCoin += quantity;
                        eachOrderBook.put(price, quantity);
                        availableCurrency -= eachTotalPrice;
                    }
                }
                if(availableCurrency == 0) {
                    amounts.put(coin, availableCoin);
                    orderBooks.put(coin, eachOrderBook);
                }
            }

        });
        return new CoinBuyDTO(amounts, orderBooks);
    }

    public CoinSellDTO calculateSell(Map<String, Double> sellingAmounts) {
        Map<String, Double> amounts = new HashMap<>();
        Map<String, SortedMap<Double, Double>> orderBooks = new HashMap<>();

        Map<String, Object> bithumbResponse = bithumbFeignClient.getOrderBook().getData();
        bithumbResponse.forEach((k,v) -> {
            if(!(k.equalsIgnoreCase("timestamp") || k.equalsIgnoreCase("payment_currency"))) {
                String coin = k;
                double sellCurrency = 0;
                Double availableCoin = sellingAmounts.get(coin);
                if(availableCoin != null) {
                    SortedMap<Double, Double> eachOrderBook = new TreeMap<>(Comparator.reverseOrder());
                    List<Map<String, String>> wannaBuy = (List<Map<String, String>> )((Map<String, Object>)v).get("bids");

                    // 내림차순으로 정렬
                    wannaBuy.sort( (k1, k2) -> {
                        if ( Double.parseDouble(k1.get("price")) < Double.parseDouble(k2.get("price"))) return 1;
                        return -1;
                    });

                    //wannaBuy.sort(Comparator.comparingDouble(k1 -> Double.parseDouble(((Map<String, String>)k1).get("price")))).reversed();

                    for(int i=0; i<wannaBuy.size(); i++) {
                        Double price = Double.parseDouble(wannaBuy.get(i).get("price"));
                        Double quantity = Double.parseDouble(wannaBuy.get(i).get("quantity"));
                        Double eachTotalPrice = price * quantity;

                        // 만약 코인 양 더 많으면 끝내기
                        if(quantity >= availableCoin) { // 못넘어갈경우
                            sellCurrency += price * availableCoin;
                            eachOrderBook.put(price, availableCoin);
                            availableCoin = 0D;
                            break;
                        } else { // 다음 스텝 넘어갈경우
                            sellCurrency += eachTotalPrice;
                            eachOrderBook.put(price, quantity);
                            availableCoin -= quantity;
                        }
                    }
                    // 모두 팔지 못했을때 조건 추가 > 넣지 말기
                    if(availableCoin == 0) {
                        amounts.put(coin, sellCurrency);
                        orderBooks.put(coin, eachOrderBook);
                    }
                }
            }
        });
        return new CoinSellDTO(amounts, orderBooks);
    }

    public Map<String /*Coin Name*/ , Double/* Withdrawal Fee */> calculateFee() throws Exception{
        Map<String, Double> result = new HashMap<>();
        Document doc = Jsoup.connect(feeUrl).timeout(60000).get();
        Elements elements = doc.select("table.fee_in_out tbody tr");

        for(Element element: elements) {
            String coinHtml = element.select("td.money_type.tx_c").html();
            if(!coinHtml.contains("(")) {
                continue;
            }
            coinHtml = coinHtml.substring(coinHtml.indexOf("(")+1, coinHtml.indexOf(")"));

            String coinFeeHtml = element.select("div.out_fee").html();
            if(coinFeeHtml.isEmpty()) { continue; }
            if(coinFeeHtml.equals("-")) {
                coinFeeHtml = "0";
            }

            result.put(coinHtml, Double.parseDouble(coinFeeHtml));
        }
        return result;
    }



}
