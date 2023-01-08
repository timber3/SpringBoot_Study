package com.sh_38.coinhub.feign;

import com.sh_38.coinhub.model.UpbitCoinPrice;
import com.sh_38.coinhub.model.UpbitMarketCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "upbit", url = "https://api.upbit.com/v1")
public interface UpbitFeignClient {
    @GetMapping("/ticker")
    List<UpbitCoinPrice> getCoinPrice(@RequestParam("markets") String coin);

    // 마켓에서 거래중인 코인 이름들을 갖게됨.
    @GetMapping("market/all")
    List<UpbitMarketCode> getMarketCode();
}
