package com.sh_38.coinhub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CommonMarketService {

    private final Map<String, MarketService> marketServices;

    public double getPrice(String market, String coin)
    {
        MarketService marketService = getMarketService(marketServices, market);

        return marketService.getCoinCurrentPrice(coin);
    }

    // 마켓들의 공통의 코인을 구해서 리턴
    public List<String> getCommonCoin (String fromMarket, String toMarket)
    {
        // 마켓 서비스를 가져온다.
        MarketService fromMarketService = getMarketService(marketServices, fromMarket);
        MarketService toMarketService = getMarketService(marketServices, toMarket);

        // 각 마켓의 거래 가능 코인 불러오기
        List<String> fromMarketCoins = fromMarketService.getCoins();
        List<String> toMarketCoins = toMarketService.getCoins();

        // 공통 코인 찾기
        List<String> result = new ArrayList<>();
        for (String eachCoin: fromMarketCoins)
        {
            if (toMarketCoins.contains(eachCoin))
            {
                result.add(eachCoin);
            }
        }
        return result;
    }

    public static MarketService getMarketService(
            Map<String, MarketService> marketServices,
            String market
    )
    {
        for(String key: marketServices.keySet())
        {
            if(key.substring(0, market.length()).equals(market.toLowerCase()))
            {
                return marketServices.get(key);
            }
        }
        return null;
    }
}

