package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.dto.CoinSellDTO;
import com.sh_38.coinhub.feign.UpbitFeeFeignClient;
import com.sh_38.coinhub.feign.UpbitFeignClient;
import com.sh_38.coinhub.model.UpbitEachWithdrawFee;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpbitMarketService implements MarketService{

    private final UpbitFeignClient upbitFeignClient;

    private final UpbitFeeFeignClient upbitFeeFeignClient;

    @Override
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

    public CoinBuyDTO calculateBuy(List<String> commonCoins, double amount)
    {
        return null;
    }

    public CoinSellDTO calculateSell(CoinBuyDTO buyDTO)
    {
        return null;
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
