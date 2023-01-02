package com.sh_38.coinhub.controller;

import com.sh_38.coinhub.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {
    @Autowired
    private MarketService marketService;
    @GetMapping("/price")
    // 코인의 최근 가격을 가져온다. : 어떤 마켓에 어떤 코인인지
    public double getPrice(
            @RequestParam String market,
            @RequestParam String coin
    )
    {
        // marketService에 getPrice 메소드를 사용할건데
        // 거기에는 market과 coin 정보가 필요하다.
        return marketService.getPrice(market, coin);
    }
}
