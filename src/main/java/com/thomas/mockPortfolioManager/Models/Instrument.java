package com.thomas.mockPortfolioManager.Models;

import javax.persistence.*;

@Entity
public abstract class Instrument {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long instrumentId;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private String underlyingTicker;

    public Instrument(String ticker, String underlyingTicker) {
        this.ticker = ticker;
        this.underlyingTicker = underlyingTicker;
    }

    public Instrument() {
    }

    public String getUnderlyingTicker() {
        return underlyingTicker;
    }

    public void setUnderlyingTicker(String underlyingTicker) {
        this.underlyingTicker = underlyingTicker;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
