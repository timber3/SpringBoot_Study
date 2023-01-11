package com.sh_38.coinhub.service;

import com.sh_38.coinhub.dto.CoinBuyDTO;
import com.sh_38.coinhub.feign.BithumbFeignClient;
import com.sh_38.coinhub.feign.response.BithumbResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BithumbMarketServiceTest {

    @Mock
    private BithumbFeignClient bithumbFeignClient;

    @InjectMocks
    private BithumbMarketService bithumbMarketService;

    @Test
    void caculateBuyTest() {
        // given
        List<String> commonCoin = List.of("A", "B");
        BithumbResponse<Map<String, Object>> mockOrderBook = mockBithumbOrderBook();

        when(bithumbFeignClient.getOrderBook()).thenReturn(mockOrderBook);
        // when
        CoinBuyDTO result = bithumbMarketService.calculateBuy(commonCoin, 5);
        // then
        // A 코인을 1원에 1개 2원에 1개 4원에 0.5개를 사야 5원을 다 쓴다.
        assertEquals(1+1+0.5, result.getAmounts().get("A"));
        assertEquals(1, result.getOrderBooks().get("A").get(1D));
        assertEquals(1, result.getOrderBooks().get("A").get(2D));
        assertEquals(0.5, result.getOrderBooks().get("A").get(3D));
        // A 코인을 1원에 2개 2원에 1.5개 4원에 0개를 사야 5원을 다 쓴다.
        assertEquals(2+1.5, result.getAmounts().get("B"));
        assertEquals(2, result.getOrderBooks().get("B").get(1D));
        assertEquals(1.5, result.getOrderBooks().get("B").get(2D));
        assertEquals(0, result.getOrderBooks().get("B").get(4D));
        // A 코인을 1원에 3개 2원에 1개 4원에 0개를 사야 5원을 다 쓴다.
        assertEquals(3+1, result.getAmounts().get("C"));
        assertEquals(3, result.getOrderBooks().get("C").get(1D));
        assertEquals(1, result.getOrderBooks().get("C").get(2D));
        assertEquals(0, result.getOrderBooks().get("C").get(4D));
    }

    private BithumbResponse<Map<String, Object>> mockBithumbOrderBook()
    {
        BithumbResponse<Map<String, Object>> result = new BithumbResponse();
        result.setData(
                Map.of(
                        "A", Map.of(
                                "bids" , List.of( // bids = wannaBuy
                                        Map.of("price", "4", "quantity" , "1"),
                                        Map.of("price", "2", "quantity" , "1"),
                                        Map.of("price", "1", "quantity" , "1")
                                ),
                                "asks" , List.of( // asks = wannaSell
                                        Map.of("price", "1", "quantity" , "1"),
                                        Map.of("price", "2", "quantity" , "1"),
                                        Map.of("price", "4", "quantity" , "1")
                                )
                        ),
                        "B", Map.of(
                                "bids" , List.of( // bids = wannaBuy
                                        Map.of("price", "4", "quantity" , "2"),
                                        Map.of("price", "2", "quantity" , "2"),
                                        Map.of("price", "1", "quantity" , "2")
                                ),
                                "asks" , List.of( // asks = wannaSell
                                        Map.of("price", "1", "quantity" , "2"),
                                        Map.of("price", "2", "quantity" , "2"),
                                        Map.of("price", "4", "quantity" , "2")
                                )
                        ),
                        "C", Map.of(
                                "bids" , List.of( // bids = wannaBuy
                                        Map.of("price", "4", "quantity" , "3"),
                                        Map.of("price", "2", "quantity" , "3"),
                                        Map.of("price", "1", "quantity" , "3")
                                ),
                                "asks" , List.of( // asks = wannaSell
                                        Map.of("price", "1", "quantity" , "3"),
                                        Map.of("price", "2", "quantity" , "3"),
                                        Map.of("price", "4", "quantity" , "3")
                                )
                        )

                )
        );

        return result;
    }

}
