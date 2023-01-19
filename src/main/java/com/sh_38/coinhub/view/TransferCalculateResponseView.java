package com.sh_38.coinhub.view;

import com.sh_38.coinhub.dto.TransferCalculateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class TransferCalculateResponseView {
    private String coin;
    private double buyamount;
    private double fee;
    private double sellamount;
    private Map<Double, Double> buyOrderBook;
    private Map<Double, Double> sellOrderBook;

    // DTO -> View
    public static TransferCalculateResponseView of(TransferCalculateDTO dto) {
        return new TransferCalculateResponseView(
                dto.getCoin(),
                dto.getBuyAmount(),
                dto.getFee(),
                dto.getSellAmount(),
                dto.getBuyOrderBook(),
                dto.getSellOrderBook()
        );
    }
}
