package com.thomas.mockPortfolioManager.Services.Portflio;

import com.google.common.collect.ImmutableList;
import com.thomas.mockPortfolioManager.Models.Instrument;
import com.thomas.mockPortfolioManager.Models.Product;
import com.thomas.mockPortfolioManager.Models.Stock;
import com.thomas.mockPortfolioManager.Models.Vanilla;
import com.thomas.mockPortfolioManager.Repositories.InstrumentRepository;
import com.thomas.mockPortfolioManager.Repositories.ProductRepository;
import com.thomas.mockPortfolioManager.Services.MarketData.PortfolioUpdatePublisher;
import com.thomas.mockPortfolioManager.Services.PriceCalculator.PriceCalculatorInterface;
import com.thomas.mockPortfolioManager.Services.PriceCalculator.StockCalculator;
import com.thomas.mockPortfolioManager.Services.PriceCalculator.VanillaCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PortfolioPriceUpdateService {
    private final Logger LOGGER = LoggerFactory.getLogger(PortfolioPriceUpdateService.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private VanillaCalculator vanillaCalculator;
    @Autowired
    private StockCalculator stockCalculator;
    @Autowired
    private PortfolioUpdatePublisher portfolioUpdatePublisher;

    public PortfolioPriceUpdateService() {
    }

    public PortfolioPriceUpdateService(ProductRepository productRepository, InstrumentRepository instrumentRepository, VanillaCalculator vanillaCalculator, StockCalculator stockCalculator, PortfolioUpdatePublisher portfolioUpdatePublisher) {
        this.productRepository = productRepository;
        this.instrumentRepository = instrumentRepository;
        this.vanillaCalculator = vanillaCalculator;
        this.stockCalculator = stockCalculator;
        this.portfolioUpdatePublisher = portfolioUpdatePublisher;
    }

    public void processPriceChange(Map<String, BigDecimal> incomingMessage) {
        LOGGER.info("Starting price update with {}", incomingMessage);
        List<Product> portfolio = ImmutableList.copyOf(productRepository.findAll());
        //for every product in portfolio
        Map<Product, Map<String, String>> resultMap = new HashMap<>();
        PriceCalculatorInterface calculator;
        for (Product product : portfolio) {
            BigDecimal underlyingPrice = getUnderlyingPrice(incomingMessage, product.getInstrument());
            if (Objects.nonNull(underlyingPrice)) { //skip the instrument if no new update for the underlying price
                Map<String, String> productOutput = new HashMap<>();
                calculator = getCalculator(product);
                BigDecimal price = calculator.calculateInstrumentValue(product.getInstrument(), underlyingPrice);
                productOutput.put("price", price.toString());
                productOutput.put("totalPrice", calculator.calculateProductValue(product, price).toString());
                productOutput.put("underlying", product.getInstrument().getUnderlyingTicker());
                productOutput.put("newPrice", underlyingPrice.toString());
                resultMap.put(product, productOutput);
            }
        }
        if (!resultMap.isEmpty()) {
            LOGGER.info("Publishing portfolio price update {}", resultMap);
            portfolioUpdatePublisher.publishPriceChange(resultMap);
        }
    }

    private BigDecimal getUnderlyingPrice(Map<String, BigDecimal> priceUpdate, Instrument instrument) {
        return priceUpdate.get(instrument.getUnderlyingTicker());
    }

    private PriceCalculatorInterface getCalculator(Product product) {
        Instrument instrument = product.getInstrument();
        if (instrument instanceof Stock) {
            return stockCalculator;
        } else if (instrument instanceof Vanilla) {
            return vanillaCalculator;
        }
        throw new IllegalStateException("Unexpected instrument type");
    }
}
