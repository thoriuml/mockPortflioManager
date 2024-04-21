package com.thomas.mockPortfolioManager.Services.MarketData;

import com.thomas.mockPortfolioManager.Models.Product;
import com.thomas.mockPortfolioManager.Services.Portflio.PortfolioViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

@Service
public class PortfolioViewListener {
    @Autowired
    private PortfolioViewService portfolioViewService;
    private ListenableQueue<Map<Product, Map<String, String>>> incomingViewUpdate = new ListenableQueue<>(new LinkedList<>());
    private final Logger LOGGER = LoggerFactory.getLogger(PortfolioViewListener.class);

    public PortfolioViewListener(PortfolioViewService portfolioViewService, ListenableQueue<Map<Product, Map<String, String>>> incomingViewUpdate) {
        this.portfolioViewService = portfolioViewService;
        this.incomingViewUpdate = incomingViewUpdate;
    }

    public PortfolioViewListener() {
        incomingViewUpdate.registerListener(this::processViewUpdate);
    }

    public void incomingViewUpdate(Map<Product, Map<String, String>> incomingMessage) { //mock behaviour that new market data is published and pushed to queue
        LOGGER.info("Received incoming update {}", incomingMessage);
        incomingViewUpdate.add(incomingMessage);
    }

    private void processViewUpdate(Map<Product, Map<String, String>> incomingMessage) {
        portfolioViewService.processPortfolioUpdate(incomingMessage);
    }
}
