package com.thomas.mockPortfolioManager.Services.PriceCalculator;

import com.thomas.mockPortfolioManager.Models.Product;
import com.thomas.mockPortfolioManager.Models.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StockCalculatorTest {

    private StockCalculator stockCalculator = new StockCalculator();

    @Test
    public void testCalculateInstrumentValue() {
        Stock stock = new Stock("AAPL");
        BigDecimal underlyingPrice = BigDecimal.valueOf(100);

        BigDecimal result = stockCalculator.calculateInstrumentValue(stock, underlyingPrice);

        assertNotNull(result);
        assertEquals(result, BigDecimal.valueOf(100));
    }

    @Test
    public void testCalculateProductValue() {
        Product product = new Product(BigDecimal.valueOf(100), "summary");
        BigDecimal price = BigDecimal.valueOf(100);

        BigDecimal result = stockCalculator.calculateProductValue(product, price);

        assertNotNull(result);
        assertEquals(result, BigDecimal.valueOf(10000));
    }
}