package com.sh_38.coinhub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommonMarketServiceTest {

    @Mock
    private BithumbMarketService bithumbMarketService;
    @Mock
    private UpbitMarketService upbitMarketService;

    private CommonMarketService commonMarketService;

    @BeforeEach
    void setup() {
        commonMarketService = new CommonMarketService(
                Map.of("bithumbMarketService", bithumbMarketService,
                        "upbitMarketService", upbitMarketService));
    }
    @Test
    void getPriceTest() {
        // given : 주어진 상황이 이럴때
        double testAmount = 123.345;
        String testCoin = "TestCoin";
        when(bithumbMarketService.getCoinCurrentPrice(testCoin)).thenReturn(123.345);
        when(upbitMarketService.getCoinCurrentPrice(testCoin)).thenReturn(123.345);


        // when : 이것들을 실행하면?
        // then : 어떤 결과가 나올지
        assertEquals(testAmount, commonMarketService.getPrice("bithumb", testCoin));
        assertEquals(testAmount, commonMarketService.getPrice("upbit", testCoin));

    }
    @Test
    void getMarketServiceTest()
    {
        // given :
        Map<String, MarketService> marketServices = new HashMap<>();
        marketServices.put("bithumbMarketService", bithumbMarketService);
        marketServices.put("upbitMarketService", upbitMarketService);

        // when : getMarketService 메소드를 실행했을 때
        // then : bithumbMarketService 와 메소드의 리턴값이 같은 결과가 나오는가?
        assertEquals(bithumbMarketService, CommonMarketService.getMarketService(marketServices, "bithumb"));
        assertEquals(bithumbMarketService, CommonMarketService.getMarketService(marketServices, "BithumB"));
        assertEquals(bithumbMarketService, CommonMarketService.getMarketService(marketServices, "upbit"));
        assertEquals(bithumbMarketService, CommonMarketService.getMarketService(marketServices, "UpbiT"));



    }


}