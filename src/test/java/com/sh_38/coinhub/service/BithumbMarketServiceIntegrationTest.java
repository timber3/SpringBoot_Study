package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.feign.BithumbFeignClient;
import com.sh_38.coinhub.feign.response.BithumbResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

//@Disabled
@SpringBootTest
@EnableFeignClients
public class BithumbMarketServiceIntegrationTest {

    @Autowired
    private BithumbMarketService bithumbMarketService;


    @Test
    void caculateFeeTest() throws Exception {
        Map<String, Double> result = bithumbMarketService.calculateFee();
        // 받아 와 지나? 확인하는 용도
        assertFalse(result.isEmpty());
    }


}
