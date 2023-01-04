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
        return 123.2222;
    }
}
