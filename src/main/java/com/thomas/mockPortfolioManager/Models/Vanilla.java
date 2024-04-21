package com.thomas.mockPortfolioManager.Models;

import com.thomas.mockPortfolioManager.Enum.OptionType;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Vanilla extends Instrument{
    private LocalDate maturity;
    private OptionType optionType;
    private BigDecimal strike;

    public Vanilla(String ticker, LocalDate maturity, OptionType optionType, BigDecimal strike, String underlying) {
        super(ticker, underlying);
        this.maturity = maturity;
        this.optionType = optionType;
        this.strike = strike;
    }

    public Vanilla() {
        super(null, null);
    }

    public LocalDate getMaturity() {
        return maturity;
    }

    public void setMaturity(LocalDate maturity) {
        this.maturity = maturity;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public BigDecimal getStrike() {
        return strike;
    }

    public void setStrike(BigDecimal strike) {
        this.strike = strike;
    }
}
