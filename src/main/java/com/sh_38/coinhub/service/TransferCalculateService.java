package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.dto.CoinSellDTO;
import com.sh_38.coinhub.dto.TransferCalculateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferCalculateService {
    private final CommonMarketService commonMarketService;
    private final Map<String, MarketService> marketServices;

    public List<TransferCalculateDTO> calculate(String fromMarket, String toMarket, double amount) throws Exception{

        // from, to : common coin
        List<String> commonCoins = commonMarketService.getCommonCoin(fromMarket, toMarket);

        MarketService fromMarketService = CommonMarketService.getMarketService(marketServices, fromMarket);
        MarketService toMarketService = CommonMarketService.getMarketService(marketServices, toMarket);

        // from 얼마에 살수있는지
        CoinBuyDTO fromMarketBuyDto = fromMarketService.calculateBuy(commonCoins, amount);
        // from 이체 수수료 coin Name, withdral fee
        Map<String, Double> fromMarketTransferFee = fromMarketService.calculateFee();

        Map<String, Double> amountAfterFee = fromMarketBuyDto.afterTransferFee(fromMarketTransferFee);


        // to 얼마에 팔수 있는지
        CoinSellDTO toMarketSellDto = toMarketService.calculateSell(amountAfterFee);


        // 가장 높은 값 Top10 코인들을 선택
        List<String> topTenCoins = toMarketSellDto.getAmounts()
                .entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10).map(Map.Entry::getKey).toList();


        List<TransferCalculateDTO> result = new ArrayList<>();

        topTenCoins.forEach(coin -> result.add(
                new TransferCalculateDTO(
                        coin,
                    fromMarketBuyDto.getAmounts().get(coin),
                    fromMarketTransferFee.get(coin),
                    toMarketSellDto.getAmounts().get(coin),
                    fromMarketBuyDto.getOrderBooks().get(coin),
                    toMarketSellDto.getOrderBooks().get(coin)
                )));

        return result;
    }
}
