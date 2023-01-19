package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.dto.CoinSellDTO;

import java.util.List;
import java.util.Map;

public interface MarketService {
    double getCoinCurrentPrice(String coin);
    List<String> getCoins();
    CoinBuyDTO calculateBuy(List<String> commonCoins, double amount);
    CoinSellDTO calculateSell(Map<String, Double> amounts);
    Map<String /*Coin Name*/ , Double/* Withdrawal Fee */> calculateFee() throws Exception;
}
