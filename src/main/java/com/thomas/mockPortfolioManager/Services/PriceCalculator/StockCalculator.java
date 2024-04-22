package com.thomas.mockPortfolioManager.Services.PriceCalculator;

import com.thomas.mockPortfolioManager.Models.Instrument;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StockCalculator implements PriceCalculatorInterface {
    @Override
    public BigDecimal calculateInstrumentValue(Instrument instrument, BigDecimal underlyingPrice) {
        return underlyingPrice;
    }

}
