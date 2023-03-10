package com.sh_38.coinhub.feign;

import com.sh_38.coinhub.model.UpbitCoinPrice;
import com.sh_38.coinhub.model.UpbitMarketCode;
import com.sh_38.coinhub.model.UpbitOrderBooks;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "upbit", url = "https://api.upbit.com/v1")
public interface UpbitFeignClient {
    @GetMapping("/ticker")
    List<UpbitCoinPrice> getCoinPrice(@RequestParam("markets") String coin);

    @GetMapping("/market/all")
    List<UpbitMarketCode> getMarketCode();

    @GetMapping("/orderbook")
    List<UpbitOrderBooks> getOrderBooks(@RequestParam List<String> markets);
}
