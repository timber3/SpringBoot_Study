package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.dto.CoinSellDTO;
import com.sh_38.coinhub.feign.UpbitFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpbitMarketService implements MarketService{

    private final UpbitFeignClient upbitFeignClient;

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
}
