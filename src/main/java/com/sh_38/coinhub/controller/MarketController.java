package com.sh_38.coinhub.controller;

import com.sh_38.coinhub.service.CommonMarketService;
import com.sh_38.coinhub.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MarketController {

    private final CommonMarketService commonMarketService;
    private final Map<String ,MarketService> marketServices;


    @GetMapping("/price")
    // 코인의 최근 가격을 가져온다. : 어떤 마켓에 어떤 코인인지
    public double getPrice(
            @RequestParam String market,
            @RequestParam String coin
    ) {
        // marketService에 getPrice 메소드를 사용할건데
        // 거기에는 market과 coin 정보가 필요하다.
        return commonMarketService.getPrice(market, coin);
    }

    @GetMapping("/coins") // 코인의 최근 가격 : 어떤 마켓, 어떤 코인인지
    public List<String> getCoins(@RequestParam String market)
    {
        return CommonMarketService.getMarketService(marketServices, market).getCoins();
    }

    @GetMapping("/common-coins") // 코인의 최근 가격 : 어떤 마켓, 어떤 코인인지
    public List<String> getCommonCoins(@RequestParam String market1, @RequestParam String market2)
    {
        return commonMarketService.getCommonCoin(market1, market2);
    }
}
