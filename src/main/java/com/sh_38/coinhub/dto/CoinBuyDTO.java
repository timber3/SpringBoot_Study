package com.sh_38.coinhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CoinBuyDTO {
    // 살 수 있는 코인의 양
    private Map<String, Double> amounts;

    // 해당 코인마다의 호가창 정보 ( 값, 양 )
    private Map<String, Map<Double, Double>> orderBooks;
}
