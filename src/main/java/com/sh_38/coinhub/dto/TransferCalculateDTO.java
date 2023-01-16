package com.sh_38.coinhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/*
@Getter
@AllArgsConstructor
public class TransferCalculateDTO {
    // 최종적으로 어떤 코인을 선택했는지
    private String coin;

    // 팔았을 때 얼만큼의 현금이 생기는지
    private double amount;

    // 얼마에 사고 얼마에 팔지에 대한 orderBook
    private Map<Double, Double> buyOrderBook;
    private Map<Double, Double> sellOrderBook;

}
*/

@Getter
@AllArgsConstructor
public class TransferCalculateDTO {
    private String coin;
    private double amount;
    private Map<Double, Double> buyOrderBook;
    private Map<Double, Double> sellOrderBook;
}