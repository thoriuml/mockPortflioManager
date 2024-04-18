package com.thomas.mockPortfolioManager.Models;

import javax.persistence.Entity;

@Entity
public class Stock extends Instrument{
    public Stock(String ticker) {
        super(ticker);
    }

    public Stock() {
        super(null);
    }
}
