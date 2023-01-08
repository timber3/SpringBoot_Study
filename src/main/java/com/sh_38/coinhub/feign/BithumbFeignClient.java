package com.sh_38.coinhub.feign;

import com.sh_38.coinhub.feign.response.BithumbResponse;
import com.sh_38.coinhub.model.BithumbAssetEachStatus;
import com.sh_38.coinhub.model.BithumbCoinPrice;
import com.sh_38.coinhub.model.UpbitCoinPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "bithumb", url = "https://api.bithumb.com/public")
public interface BithumbFeignClient {
    @GetMapping("/ticker/{coin}")
    BithumbResponse<BithumbCoinPrice> getCoinPrice(@PathVariable("coin") String coin);

    @GetMapping("/assetsstatus/ALL")
    BithumbResponse<Map<String, BithumbAssetEachStatus>> getAssetStatus();

}
