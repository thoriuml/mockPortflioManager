package com.thomas.mockPortfolioManager.Services.MarketData;

import com.google.common.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MarketDataPublisherTest {
    @Mock
    private MarketDataListener marketDataListener = mock(MarketDataListener.class);
    @Mock
    private Cache<String, BigDecimal> cache = mock(Cache.class);
    @Mock
    private Random random = mock(Random.class);
    @Captor
    private ArgumentCaptor<Map<String, BigDecimal>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);

    private MarketDataPublisher marketDataPublisher;

    @BeforeEach
    public void setup() {
        marketDataPublisher = new MarketDataPublisher(marketDataListener, cache, random);
    }

    @Test
    public void testMockPublishMarketData() {
        Supplier<DoubleStream> streamSupplier
                = () -> DoubleStream.of(0.05);
        when(random.doubles(-0.05, 0.05)).thenAnswer(a -> streamSupplier.get());
        marketDataPublisher.mockPublishMarketData();
        Mockito.verify(marketDataListener).incomingMarketData(mapArgumentCaptor.capture());
        BigDecimal expectedNewPrice = BigDecimal.valueOf(160).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(0.05)));
        Map<String, BigDecimal> map = mapArgumentCaptor.getValue();
        assertNotNull(map);
        assertEquals(map.get("AAPL"), expectedNewPrice);

    }

}