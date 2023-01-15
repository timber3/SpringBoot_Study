package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.dto.CoinSellDTO;
import com.sh_38.coinhub.feign.UpbitFeeFeignClient;
import com.sh_38.coinhub.feign.UpbitFeignClient;
import com.sh_38.coinhub.model.UpbitEachWithdrawFee;
import com.sh_38.coinhub.model.UpbitOrderBooks;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpbitMarketService implements MarketService{

    private final UpbitFeignClient upbitFeignClient;

    private final UpbitFeeFeignClient upbitFeeFeignClient;

    public double getCoinCurrentPrice(String coin) {
        // coin은 대소문자 구분이 안되기 때문에 대문자로 만들어줌
        return upbitFeignClient.getCoinPrice("KRW-" + coin.toUpperCase())
                .get(0)
                .getTrade_price();
    }

    public List<String> getCoins()
    {
        /*
         API를 활용해서 가져와야함 (Feign Client)
         getMarketCode()를 하면 코인들의 List들이 나올 것이고
         이제 각 element들을 순회하면서 나온 값들 중에서 market 필드에 대한 값만 가져온 후
         그 값이 KRW로 시작할 경우에만 List에 저장한다.
        */
        List<String> result = new ArrayList<>();

        upbitFeignClient.getMarketCode().forEach(k -> {
            if(k.getMarket().startsWith("KRW"))
            {
                result.add(k.getMarket().substring(4).toUpperCase());
            }

        });

        return result;
    }

    public CoinBuyDTO calculateBuy(List<String> commonCoins, double amount) {
        Map<String, Double> amounts = new HashMap<>();
        Map<String, Map<Double, Double>> orderBooks = new HashMap<>();
        commonCoins.stream().map(k -> "KRW-" + k.toUpperCase()).toList();

        List<UpbitOrderBooks> bithumbResponse = upbitFeignClient.getOrderBooks(commonCoins);
        bithumbResponse.forEach(k -> {
            double availableCurrency = amount;
            double availableCoin = 0;
            String coin = k.getMarket().substring(4);
            Map<Double, Double> eachOrderBook = new HashMap<>();

            List<UpbitOrderBooks.UpbitEachOrderBooks> eachOrderBooks = k.getOrderbook_units();
            for(int i=0; i<eachOrderBooks.size(); i++) {
                Double price = eachOrderBooks.get(i).getAsk_price();
                Double quantity = eachOrderBooks.get(i).getAsk_size();
                Double eachTotalPrice = price * quantity;
                double buyableCoinAmount = availableCurrency/price;

                // 만약 가격 넘으면 다음스텝,아니면 끝내기
                if(eachTotalPrice >= availableCurrency) { // 못넘어갈경우
                    availableCoin += buyableCoinAmount;
                    eachOrderBook.put(price, buyableCoinAmount);
                    availableCurrency = 0;
                    break;
                } else { // 다음 스텝 넘어갈경우
                    availableCoin += quantity;
                    eachOrderBook.put(price, quantity);
                    availableCurrency -= price * quantity;
                }
            }

            // 금액 모두 맞추지 못했을때 조건 추가 > 넣지 말기
            if(availableCurrency == 0) {
                amounts.put(coin, availableCoin);
                orderBooks.put(coin, eachOrderBook);
            }
        });

        return new CoinBuyDTO(amounts, orderBooks);
    }

    public CoinSellDTO calculateSell(CoinBuyDTO buyDTO) {
        Map<String, Double> sellingAmounts = buyDTO.getAmounts();
        Map<String, Double> amounts = new HashMap<>();
        Map<String, Map<Double, Double>> orderBooks = new HashMap<>();
        List<String> coins = sellingAmounts.keySet().stream().map(k -> "KRW-" + k.toUpperCase()).toList();

        List<UpbitOrderBooks> upbitResponse = upbitFeignClient.getOrderBooks(coins);

        upbitResponse.forEach(k -> {
            String coin = k.getMarket().substring(4);
            double sellCurrency = 0;
            Double availableCoin = sellingAmounts.get(coin);

            if(availableCoin != null) {
                Map<Double, Double> eachOrderBook = new HashMap<>();
                List<UpbitOrderBooks.UpbitEachOrderBooks> eachOrderBooks = k.getOrderbook_units();
                for(int i=0; i<eachOrderBooks.size(); i++) {
                    Double price = eachOrderBooks.get(i).getBid_price();
                    Double quantity = eachOrderBooks.get(i).getBid_size();
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
        });

        return new CoinSellDTO(amounts, orderBooks);
    }

    public Map<String /* Coin name */ , Double /* Withdraw Fee */> calculateFee() throws Exception
    {
        return upbitFeeFeignClient.getWithdrawFee().getData()
                .stream()
                .collect(Collectors.toMap(
                        // key값으로 무엇을 가져올 것인지?
                        /*k -> k.getCurrency(),
                        k -> k.getWithdrawFee()*/
                        UpbitEachWithdrawFee::getCurrency,
                        UpbitEachWithdrawFee::getWithdrawFee
                ));
    }
}
