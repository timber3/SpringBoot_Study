package com.sh_38.coinhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@Getter
@AllArgsConstructor
public class CoinBuyDTO {
    private Map<String, Double> amounts;
    private Map<String, SortedMap<Double, Double>> orderBooks;

    // coin name, amount
    public Map<String, Double> afterTransferFee(
            Map<String, Double> fromMarketTransferFee
    ) {
        Map<String, Double> result = new HashMap<>();

        amounts.forEach((k,v) -> {
            if (fromMarketTransferFee.containsKey(k)) {
                result.put(k, amounts.get(k) - fromMarketTransferFee.get(k));
            }
        });

        return amounts;
    }
}
