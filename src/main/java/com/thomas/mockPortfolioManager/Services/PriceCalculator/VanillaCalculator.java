package com.thomas.mockPortfolioManager.Services.PriceCalculator;

import com.thomas.mockPortfolioManager.Enum.OptionType;
import com.thomas.mockPortfolioManager.Models.Instrument;
import com.thomas.mockPortfolioManager.Models.Vanilla;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.temporal.ChronoUnit;

@Service
public class VanillaCalculator implements PriceCalculatorInterface { //european style
    @Autowired
    private LocalDateProvider localDateProvider;
    private final NormalDistribution normalDistribution = new NormalDistribution();

    private final BigDecimal DAYS_IN_YEAR = BigDecimal.valueOf(365);//assuming always non leap year here
    private final BigDecimal INTEREST_RATE = BigDecimal.valueOf(0.02); //static value for risk free interest
    private final BigDecimal VOL = BigDecimal.valueOf(0.15); //static value for volatility


    public VanillaCalculator() {
    }

    public VanillaCalculator(LocalDateProvider localDateProvider) {
        this.localDateProvider = localDateProvider;
    }

    @Override
    public BigDecimal calculateInstrumentValue(Instrument instrument, BigDecimal underlyingPrice) {
        Vanilla vanilla = (Vanilla) instrument;
        //prepare values and convert to double for calculation
        double underlyingPriceDouble = underlyingPrice.doubleValue();
        double strikeDouble = vanilla.getStrike().doubleValue();
        double interestDouble = INTEREST_RATE.doubleValue();
        double yearsToMaturityDouble = resolveYearsToMaturity(vanilla).doubleValue();
        double volDouble = VOL.doubleValue();

        double d1 = getD1Value(underlyingPriceDouble, strikeDouble, interestDouble, yearsToMaturityDouble, volDouble);
        double d2 = getD2Value(d1, yearsToMaturityDouble, volDouble);

        if (vanilla.getOptionType() == OptionType.C) {
            return BigDecimal.valueOf(underlyingPriceDouble * normalDistribution.cumulativeProbability(d1)
                    - strikeDouble * Math.exp(-interestDouble * yearsToMaturityDouble)
                    * normalDistribution.cumulativeProbability(d2));
        } else { //else is put
            return BigDecimal.valueOf(strikeDouble * Math.exp(-interestDouble * yearsToMaturityDouble)
                    * normalDistribution.cumulativeProbability(-d2) - underlyingPriceDouble
                    * normalDistribution.cumulativeProbability(-d1));
        }
    }

    private double getD1Value(double underlyingPrice, double strike, double interest, double timeToMaturity, double vol) {
        double top = Math.log(underlyingPrice / strike) + (interest + Math.pow(vol, 2) / 2) * timeToMaturity;
        double bottom = vol * Math.sqrt(timeToMaturity);
        return top / bottom;
    }

    private double getD2Value(double d1Value, double timeToMaturity, double vol) {
        return d1Value - vol * Math.sqrt(timeToMaturity);
    }


    private BigDecimal resolveYearsToMaturity(Vanilla vanilla) { //return t value by the days between now and maturity
//        BigDecimal dayDiff = BigDecimal.valueOf(vanilla.getMaturity().until(localDateProvider.getLocalDate(), ChronoUnit.DAYS));
        BigDecimal dayDiff = BigDecimal.valueOf(localDateProvider.getLocalDate().until(vanilla.getMaturity(), ChronoUnit.DAYS));
        return dayDiff.divide(DAYS_IN_YEAR, MathContext.DECIMAL128);
    }
}
