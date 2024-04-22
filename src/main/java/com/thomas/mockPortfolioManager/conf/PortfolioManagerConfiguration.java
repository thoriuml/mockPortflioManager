package com.thomas.mockPortfolioManager.conf;

import com.thomas.mockPortfolioManager.Services.MarketData.MarketDataPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class PortfolioManagerConfiguration {
    @Autowired
    private MarketDataPublisher marketDataPublisher;

    @Scheduled(cron = "${portfolio.marketData.interval.cron}")
    public void mockPublishMarketData() {
        marketDataPublisher.mockPublishMarketData();
    }
}
