package com.thomas.mockPortfolioManager.Services.PriceCalculator;

import com.thomas.mockPortfolioManager.Models.Instrument;
import com.thomas.mockPortfolioManager.Models.Product;

import java.math.BigDecimal;

public interface PriceCalculatorInterface {
    public BigDecimal calculateInstrumentValue(Instrument instrument, BigDecimal underlyingPrice);

    default BigDecimal calculateProductValue(Product product, BigDecimal price) { //total price = price * size
        return product.getSize().multiply(price);
    }
}
