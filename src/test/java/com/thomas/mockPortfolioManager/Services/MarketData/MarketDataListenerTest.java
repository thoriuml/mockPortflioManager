package com.thomas.mockPortfolioManager.Services.MarketData;

import com.google.common.collect.ImmutableMap;
import com.thomas.mockPortfolioManager.Services.Portflio.PortfolioPriceUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class MarketDataListenerTest {
    @Mock
    private PortfolioPriceUpdateService portfolioPriceUpdateService = mock(PortfolioPriceUpdateService.class);
    @Mock
    private ListenableQueue<Map<String, BigDecimal>> incomingMarketUpdate = mock(ListenableQueue.class);
    private MarketDataListener marketDataListener;
    @Captor
    private ArgumentCaptor<Map<String, BigDecimal>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);

    @BeforeEach
    public void setup() {
        marketDataListener = new MarketDataListener(portfolioPriceUpdateService, incomingMarketUpdate);
    }

    @Test
    public void testIncomingMarketData() {
        Map<String, BigDecimal> incomingMessage = ImmutableMap.of("TICKER", BigDecimal.valueOf(1000));
        marketDataListener.incomingMarketData(incomingMessage);
        Mockito.verify(incomingMarketUpdate).add(mapArgumentCaptor.capture());
        Map<String, BigDecimal> map = mapArgumentCaptor.getValue();
        assertNotNull(map);
        assertEquals(map.get("TICKER"), BigDecimal.valueOf(1000));
    }

}