package com.thomas.mockPortfolioManager.Services.PriceCalculator;

import java.math.BigDecimal;

public interface PriceCalculatorInterface {
    public BigDecimal calculateInstrumentValue();

    public BigDecimal calculateProductValue();

}
