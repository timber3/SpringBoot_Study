package com.sh_38.coinhub.feign;

import com.sh_38.coinhub.model.UpbitCoinPrice;
import com.sh_38.coinhub.model.UpbitMarketCode;
import com.sh_38.coinhub.model.UpbitOrderBooks;
import com.sh_38.coinhub.model.UpbitWithdrawFee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "upbitFee", url = "https://api-manager.upbit.com/api/v1/kv")
public interface UpbitFeeFeignClient {
    @GetMapping("/UPBIT_PC_COIN_DEPOSIT_AND_WITHDRAW_GUIDE")
    UpbitWithdrawFee getWithdrawFee();

}
