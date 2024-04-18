package com.thomas.mockPortfolioManager.Models;

import javax.persistence.*;

@Entity
public abstract class Instrument {

    public Instrument(String ticker) {
        this.ticker = ticker;
    }

    public Instrument() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long instrumentId;

    @Column(nullable = false)
    private String ticker;

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
