package com.thomas.mockPortfolioManager.Services.Portflio;

import com.thomas.mockPortfolioManager.Models.Product;
import com.thomas.mockPortfolioManager.Models.Stock;
import com.thomas.mockPortfolioManager.Repositories.InstrumentRepository;
import com.thomas.mockPortfolioManager.Repositories.ProductRepository;
import com.thomas.mockPortfolioManager.Services.MarketData.PortfolioUpdatePublisher;
import com.thomas.mockPortfolioManager.Services.PriceCalculator.StockCalculator;
import com.thomas.mockPortfolioManager.Services.PriceCalculator.VanillaCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PortfolioPriceUpdateServiceTest {
    @Mock
    private ProductRepository productRepository = mock(ProductRepository.class);
    @Mock
    private InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    @Mock
    private VanillaCalculator vanillaCalculator = mock(VanillaCalculator.class);
    @Mock
    private StockCalculator stockCalculator = mock(StockCalculator.class);
    @Mock
    private PortfolioUpdatePublisher portfolioUpdatePublisher = mock(PortfolioUpdatePublisher.class);
    private PortfolioPriceUpdateService portfolioPriceUpdateService;
    @Captor
    private ArgumentCaptor<Map<Product, Map<String, String>>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);

    @BeforeEach
    public void setup() {
        portfolioPriceUpdateService = new PortfolioPriceUpdateService(productRepository, instrumentRepository, vanillaCalculator, stockCalculator, portfolioUpdatePublisher);
    }

    @Test
    public void testProcessPriceChange() {
        //setup
        Stock stock = new Stock("AAPL");
        Product product = new Product(BigDecimal.valueOf(100), "AAPL,1000");
        product.setInstrument(stock);
        Map<String, BigDecimal> incomingMessage = new HashMap<>();
        incomingMessage.put("AAPL", BigDecimal.valueOf(150));

        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        when(stockCalculator.calculateInstrumentValue(any(), any())).thenReturn(BigDecimal.valueOf(150));
        when(stockCalculator.calculateProductValue(any(), any())).thenReturn(BigDecimal.valueOf(150000));

        //run
        portfolioPriceUpdateService.processPriceChange(incomingMessage);

        //verify
        Mockito.verify(portfolioUpdatePublisher).publishPriceChange(mapArgumentCaptor.capture());
        Map<Product, Map<String, String>> map = mapArgumentCaptor.getValue();
        assertNotNull(map);
        assertEquals(map.entrySet().stream().findFirst().get().getKey(), product);
        Map<String, String> updateMap = map.get(product);
        assertNotNull(updateMap);
        assertEquals(updateMap.get("price"), "150");
        assertEquals(updateMap.get("totalPrice"), "150000");
        assertEquals(updateMap.get("underlying"), "AAPL");
        assertEquals(updateMap.get("newPrice"), "150");
    }

}