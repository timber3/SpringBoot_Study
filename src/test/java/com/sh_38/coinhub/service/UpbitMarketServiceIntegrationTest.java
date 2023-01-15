package com.sh_38.coinhub.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

//@Disabled
@EnableFeignClients
@SpringBootTest
public class UpbitMarketServiceIntegrationTest {

    @Autowired
    private UpbitMarketService upbitMarketService;


    @Test
    void caculateFeeTest() throws Exception {
        Map<String, Double> result = upbitMarketService.calculateFee();
        // 받아 와 지나? 확인하는 용도
        assertFalse(result.isEmpty());
    }


}
