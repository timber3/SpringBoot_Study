package com.sh_38.coinhub.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UpbitOrderBooks {

    @Getter
    @Setter
    @AllArgsConstructor
    public class UpbitEachOrderBooks {
        private double ask_price;
        private double bid_price;
        private double ask_size;
        private double bid_size;
    }

    private String market;
    private String timestamp;
    private String total_ask_size;
    private String total_bid_size;
    private List<UpbitEachOrderBooks> orderbook_units;

}
