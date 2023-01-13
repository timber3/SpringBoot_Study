package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.dto.CoinSellDTO;
import com.sh_38.coinhub.feign.BithumbFeignClient;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BithumbMarketService implements MarketService{
    private final BithumbFeignClient bithumbFeignClient;
    @Value("${feeURL.bithumb}")
    private String feeUrl;
    @Override
    public double getCoinCurrentPrice(String coin) {
        return Double.parseDouble(
                bithumbFeignClient.getCoinPrice(coin.toUpperCase() + "_KRW")
                .getData()
                .getClosing_price());
    }

    public List<String> getCoins()
    {
        List<String> result = new ArrayList<>();

        bithumbFeignClient.getAssetStatus().getData().forEach((k,v) -> {
            if(v.getDeposit_status() == 1 && v.getWithdrawal_status() == 1)
            {
                result.add(k.toUpperCase());
            }

        });

        return result;
    }

    public CoinBuyDTO calculateBuy(List<String> commonCoins, double amount)
    {
        // 살 코인의 양
        Map<String, Double> amounts = new HashMap<>();
        // 해당 코인마다의 호가창 정보
        Map<String, Map<Double, Double>> orderBooks = new HashMap<>();

        // feign 으로 orderbook 가져오기
        Map<String, Object> bithumbResponse = bithumbFeignClient.getOrderBook().getData();

        // orderBook for문 돌기
        bithumbResponse.forEach((k,v) -> {
            // k : coin , v : obj
            // forEach 구문에서는 continue가 안됨.
            if (!(k.equalsIgnoreCase("timestamp") || k.equalsIgnoreCase("payment_currency")))
            {
                // 내가 이체할 현금
                double availableCurrency = amount;
                // 현재 살 코인의 개수
                double availableCoin = 0;

                String coin = k;
                Map<Double, Double> eachOrderBook = new HashMap<>();
                List<Map<String, String>> wannaSell =
                        (List<Map<String, String>>)((Map<String, Object>) v).get("asks");

                // 얼마나 살 수 있는지 금액을 계산하기
                for(int i = 0 ; i < wannaSell.size(); i++) {
                    Double price = Double.parseDouble(wannaSell.get(i).get("price"));
                    Double quantity = Double.parseDouble(wannaSell.get(i).get("quantity"));

                    Double eachTotalPrice = price * quantity;
                    Double buyableCoinAmount = availableCurrency / price;


                    // 이체금액 <= X : 현재 호가창에서 가장 싼 가격에 살 수 있는만큼 사고 종료
                    if (availableCurrency <= eachTotalPrice) {
                        availableCoin += buyableCoinAmount;
                        // 살 수 있는 호가창에 추가하기
                        eachOrderBook.put(price, buyableCoinAmount);
                        availableCurrency = 0;
                        break;
                    }
                    // 이체금액 > X : 다음 스텝
                    else {
                        availableCoin += quantity;
                        eachOrderBook.put(price, quantity);
                        availableCurrency -= eachTotalPrice;
                    }
                }

                if(availableCurrency == 0)
                {
                    amounts.put(coin, availableCoin);
                    orderBooks.put(coin,eachOrderBook);
                }


            }
        });

        return new CoinBuyDTO(amounts, orderBooks);
    }

    public CoinSellDTO calculateSell(CoinBuyDTO buyDTO)
    {
        return null;
    }

    public Map<String /* Coin name */ , Double /* Withdraw Fee */> calculateFee() throws Exception
    {
        Map<String, Double> result = new HashMap<>();
        // 만약에 빗썸 페이지가 개편되서 주소가 달라진다면 수정해줘야 하는데
        // 이런 데이터들은 yml 파일에 한군데로 모아둔다.
        Document doc = Jsoup.connect(feeUrl).timeout(30000).get();

        //table.fee_inout tbody tr

        Elements elements = doc.select("table.fee_in_out tbody tr");

        for(Element element: elements) {
            String coinHtml = element.select("td.money_type.tx_c").html();
            if(!coinHtml.contains("("))
            {
                continue;
            }
            coinHtml = coinHtml.substring(coinHtml.indexOf("(") + 1, coinHtml.indexOf(")"));

            String coinFeeHtml = element.select("div.out_fee").html();
            if (coinFeeHtml.isEmpty())
            {
                continue;
            }
            if(coinFeeHtml.equals("-"))
            {
                coinFeeHtml = "0";
            }


            result.put(coinHtml, Double.parseDouble(coinFeeHtml));
        }

        return result;
    }

}
