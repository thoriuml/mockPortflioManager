package com.thomas.mockPortfolioManager.Services.MarketData;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class MarketDataPublisher {
    @Autowired
    MarketDataListener marketDataListener;
    private final Logger LOGGER = LoggerFactory.getLogger(MarketDataPublisher.class);
    private Cache<String, BigDecimal> priceCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build(); //cache so we can build from the prev price
    private static final Map<String, BigDecimal> TICKERS_TO_PUBLISH = ImmutableMap.of("AAPL", BigDecimal.valueOf(160),
            "TELSA", BigDecimal.valueOf(150)); //starts off with the specified value to make it more believable
    private Random random = new Random();
    private final static Double RANDOM_LOWER_BOUND = -0.05;
    private final static Double RANDOM_UPPER_BOUND = 0.05;

    @VisibleForTesting
    public MarketDataPublisher(Cache<String, BigDecimal> priceCache, Random random) {
        this.priceCache = priceCache;
        this.random = random;
    }

    public MarketDataPublisher() {
    }

    public MarketDataPublisher(MarketDataListener marketDataListener, Cache<String, BigDecimal> priceCache, Random random) {
        this.marketDataListener = marketDataListener;
        this.priceCache = priceCache;
        this.random = random;
    }

    public void mockPublishMarketData() { //each trigger of this will push some shifted market price to marketDataListener
        Map<String, BigDecimal> publishMap = new HashMap<>();
        TICKERS_TO_PUBLISH.forEach((k, v) -> {
            publishMap.put(k, generateNewMarketPrice(k));
        });
        LOGGER.info("Publishing mock market data of {}", publishMap);
        marketDataListener.incomingMarketData(publishMap);
    }

    private BigDecimal generateNewMarketPrice(String ticker) {
        BigDecimal prevPrice = Optional.ofNullable(priceCache.getIfPresent(ticker)).orElse(TICKERS_TO_PUBLISH.get(ticker)); //use default price if prev is not present
        BigDecimal newPrice = prevPrice.multiply(BigDecimal.ONE.add(getRandomShift()), MathContext.DECIMAL128);
        priceCache.put(ticker, newPrice);
        return newPrice;
    }

    private BigDecimal getRandomShift() {
        return BigDecimal.valueOf(random.doubles(RANDOM_LOWER_BOUND, RANDOM_UPPER_BOUND).findFirst().orElseThrow(IllegalArgumentException::new));
    }

}
