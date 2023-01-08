package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.dto.CoinSellDTO;
import com.sh_38.coinhub.dto.TransferCalculateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferCalculateService {
    private final CommonMarketService commonMarketService;
    private final Map<String, MarketService> marketServices;
    public TransferCalculateDTO calculate(
            String fromMarket, String toMarket, Double amount
    )
    {
/*        // from 과 to 의 common coin 찾기
        List<String> commonCoins = commonMarketService.getCommonCoin(fromMarket, toMarket);

        MarketService fromMarketService = CommonMarketService.getCommonCoins(marketServices, fromMarket);
        MarketService toMarketService = CommonMarketService.getCommonCoins(marketServices, toMarket);


        // from 에서 얼마에 사는지
        CoinBuyDTO fromMarketBuyDto = fromMarketService.calculateBuy(commonCoins, amount);

        // from에서 이체수수료
        Map<String , Double> fromMarketTransferFee = fromMarketService.calculateFee(commonCoins, amount);

        // to에서 얼마에 파는지
        CoinSellDTO toMarketSellDto = toMarketService.calculateSell(commonCoins, amount);


        // 가장 높은 값을 받을 수 있는 코인을 선택하기.
        String transferCoin = toMarketSellDto.getAmounts().keySet().get(0);
        // TODO: 가장 많은 현금 선택

        return new TransferCalculateDTO(
                transferCoin, toMarketSellDto.getAmounts().get(transferCoin),
                fromMarketBuyDto.getOrderBooks().get(transferCoin),
                toMarketSellDto.getOrderBooks().get(transferCoin)
        );*/
        return null;
    }
}
