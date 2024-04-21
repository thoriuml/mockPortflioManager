package com.thomas.mockPortfolioManager.Services.MarketData;

import com.google.common.annotations.VisibleForTesting;
import com.thomas.mockPortfolioManager.Services.Portflio.PortfolioPriceUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Map;

@Service
public class MarketDataListener {
    @Autowired
    private PortfolioPriceUpdateService portfolioPriceUpdateService;
    private ListenableQueue<Map<String, BigDecimal>> incomingMarketUpdate = new ListenableQueue<>(new LinkedList<>());
    private final Logger LOGGER = LoggerFactory.getLogger(MarketDataListener.class);

    @VisibleForTesting
    public MarketDataListener(PortfolioPriceUpdateService portfolioPriceUpdateService, ListenableQueue<Map<String, BigDecimal>> incomingMarketUpdate) {
        this.portfolioPriceUpdateService = portfolioPriceUpdateService;
        this.incomingMarketUpdate = incomingMarketUpdate;
    }

    public MarketDataListener() {
        incomingMarketUpdate.registerListener(this::processPriceChange);
    }

    public void incomingMarketData(Map<String, BigDecimal> incomingMessage) { //mock behaviour that new market data is published and pushed to queue
        LOGGER.info("Received incoming update {}", incomingMessage);
        incomingMarketUpdate.add(incomingMessage);
    }

    private void processPriceChange(Map<String, BigDecimal> incomingMessage) {
        portfolioPriceUpdateService.processPriceChange(incomingMessage);
    }

}
