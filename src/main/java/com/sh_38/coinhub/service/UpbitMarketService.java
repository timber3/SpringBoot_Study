package com.sh_38.coinhub.service;

import com.sh_38.coinhub.feign.UpbitFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpbitMarketService implements MarketService{
    @Autowired
    UpbitFeignClient upbitFeignClient;

    @Override
    public double getCoinCurrentPrice(String coin) {
        // coin은 대소문자 구분이 안되기 때문에 대문자로 만들어줌
        return upbitFeignClient.getCoinPrice("KRW-" + coin.toUpperCase())
                .get(0)
                .getTrade_price();


    }
}
