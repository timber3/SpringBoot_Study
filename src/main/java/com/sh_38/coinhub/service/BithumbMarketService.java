package com.sh_38.coinhub.service;

import org.springframework.stereotype.Service;

@Service
public class BithumbMarketService implements MarketService{
    @Override
    public double getCoinCurrentPrice(String coin) {
        return 123.1111;
    }
}
