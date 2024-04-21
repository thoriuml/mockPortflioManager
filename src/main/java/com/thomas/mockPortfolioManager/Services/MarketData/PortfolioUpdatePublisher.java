package com.thomas.mockPortfolioManager.Services.MarketData;

import com.thomas.mockPortfolioManager.Models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PortfolioUpdatePublisher {
    @Autowired
    private PortfolioViewListener portfolioViewListener;

    public PortfolioUpdatePublisher(PortfolioViewListener portfolioViewListener) {
        this.portfolioViewListener = portfolioViewListener;
    }

    public PortfolioUpdatePublisher() {
    }

    public void publishPriceChange(Map<Product, Map<String, String>> publishMap) {
        portfolioViewListener.incomingViewUpdate(publishMap);
    }
}
