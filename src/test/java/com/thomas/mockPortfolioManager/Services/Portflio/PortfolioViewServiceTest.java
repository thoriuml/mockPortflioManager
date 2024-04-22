package com.thomas.mockPortfolioManager.Services.Portflio;

import com.thomas.mockPortfolioManager.Models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PortfolioViewServiceTest {
    private PrintStream printStream = mock(PrintStream.class);
    private PortfolioViewService portfolioViewService;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    public void setup() {
        portfolioViewService = new PortfolioViewService(printStream);
    }

    @Test
    void testProcessPortfolioUpdate() {
        //setup
        Product product = new Product(BigDecimal.valueOf(1000), "AAPL");
        Map<Product, Map<String, String>> updateMap = new HashMap<>();
        Map<String, String> detailsMap = new HashMap<>();
        detailsMap.put("totalPrice", "500000");
        detailsMap.put("price", "500");
        detailsMap.put("underlying", "AAPL");
        detailsMap.put("newPrice", "150");
        updateMap.put(product, detailsMap);

        //run
        portfolioViewService.processPortfolioUpdate(updateMap);

        //verify
        verify(printStream).printf(any());
    }
}