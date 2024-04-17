package com.thomas.mockPortfolioManager.Models;

import com.thomas.mockPortfolioManager.Enum.OptionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Vanilla extends Instrument{
    private LocalDate expiry;
    private OptionType optionType;

    private BigDecimal strike;
}
