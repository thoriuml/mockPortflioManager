package com.thomas.mockPortfolioManager.Models;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @Column(nullable = false)
    private BigDecimal size;

    @Column(nullable = false)
    private String summary;

    @ManyToOne
    @JoinColumn(name = "instrumentId")
    private Instrument instrument;

    public Product(BigDecimal size, Instrument instrument, String summary) {
        this.size = size;
        this.instrument = instrument;
        this.summary = summary;
    }

    public Product(BigDecimal size, String summary) {
        this.size = size;
        this.summary = summary;
    }

    public Product() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }
}
