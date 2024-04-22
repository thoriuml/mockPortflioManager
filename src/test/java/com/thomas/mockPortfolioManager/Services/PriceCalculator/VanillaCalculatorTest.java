package com.thomas.mockPortfolioManager.Services.PriceCalculator;

import com.thomas.mockPortfolioManager.Enum.OptionType;
import com.thomas.mockPortfolioManager.Models.Product;
import com.thomas.mockPortfolioManager.Models.Vanilla;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VanillaCalculatorTest {
    @Mock
    private LocalDateProvider localDateProvider = mock(LocalDateProvider.class);
    private VanillaCalculator vanillaCalculator;

    @BeforeEach
    public void setup() {
        vanillaCalculator = new VanillaCalculator(localDateProvider);
    }

    @Test
    public void testCalculateInstrumentValue_call() {
        //AAPL-OCT-2025-110-C,-20000
        Vanilla vanilla = new Vanilla("AAPL-OCT-2025-110-C", LocalDate.of(2025, 10, 1), OptionType.C, BigDecimal.valueOf(110), "AAPL");
        BigDecimal underlyingPrice = BigDecimal.valueOf(171.675);

        when(localDateProvider.getLocalDate()).thenReturn(LocalDate.of(2024, 4, 22));

        BigDecimal result = vanillaCalculator.calculateInstrumentValue(vanilla, underlyingPrice);

        assertNotNull(result);
        assertEquals(result.setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(64.84));
    }

    @Test
    public void testCalculateInstrumentValue_put() {
        //AAPL-OCT-2025-110-P,-20000
        Vanilla vanilla = new Vanilla("AAPL-OCT-2025-110-P", LocalDate.of(2025, 10, 1), OptionType.P, BigDecimal.valueOf(110), "AAPL");
        BigDecimal underlyingPrice = BigDecimal.valueOf(171.675);

        when(localDateProvider.getLocalDate()).thenReturn(LocalDate.of(2024, 4, 22));

        BigDecimal result = vanillaCalculator.calculateInstrumentValue(vanilla, underlyingPrice);

        assertNotNull(result);
        assertEquals(result.setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(0.03));
    }

    @Test
    public void testCalculateProductValue() {
        BigDecimal price = BigDecimal.valueOf(100);
        Product product = new Product(BigDecimal.valueOf(100), "TICKER");

        BigDecimal result = vanillaCalculator.calculateProductValue(product, price);

        assertNotNull(result);
        assertEquals(result, BigDecimal.valueOf(10000));
    }

}