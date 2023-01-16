package com.sh_38.coinhub.controller;

import com.sh_38.coinhub.service.TransferCalculateService;
import com.sh_38.coinhub.view.TransferCalculateResponseView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TransferCalculateController {

    private final TransferCalculateService transferCalculateService;

    @GetMapping("/transfer-calculate")
    public TransferCalculateResponseView getPrice(
            @RequestParam String fromMarket,
            @RequestParam String toMarket,
            @RequestParam double amount
    ) {

        return new TransferCalculateResponseView("BTC",
                12.345,
                Map.of(123D, 234D),
                Map.of(123D, 234D));
        //return TransferCalculateResponseView.of(transferCalculateService.calculate(fromMarket,toMarket,amount));
    }
}

