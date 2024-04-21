package com.thomas.mockPortfolioManager.Services.PriceCalculator;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LocalDateProvider {
    public LocalDateProvider() {
    }

    public LocalDate getLocalDate() {
        return LocalDate.now();
    }
}
