package com.thomas.mockPortfolioManager.Models;

import javax.persistence.Entity;

@Entity
public class Stock extends Instrument{
    public Stock(String ticker) {
        super(ticker, ticker);
    } //for stocks ticker and underlying is always the same

    public Stock() {
        super(null, null);
    }
}
