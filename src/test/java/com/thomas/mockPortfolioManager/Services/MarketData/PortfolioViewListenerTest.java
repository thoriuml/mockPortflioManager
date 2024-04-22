package com.thomas.mockPortfolioManager.Services.MarketData;

import com.thomas.mockPortfolioManager.Models.Product;
import com.thomas.mockPortfolioManager.Services.Portflio.PortfolioViewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;

class PortfolioViewListenerTest {
    @Mock
    private PortfolioViewService portfolioViewService = mock(PortfolioViewService.class);
    @Mock
    private ListenableQueue<Map<Product, Map<String, String>>> incomingUpdate = mock(ListenableQueue.class);
    private PortfolioViewListener portfolioViewListener;

    @BeforeEach
    public void setup() {
        portfolioViewListener = new PortfolioViewListener(portfolioViewService, incomingUpdate);
    }

    @Test
    public void testIncomingMarketData() {
        portfolioViewListener.incomingViewUpdate(new HashMap<>());
        Mockito.verify(incomingUpdate).add(anyMap());
    }

}