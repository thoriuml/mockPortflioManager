package com.thomas.mockPortfolioManager.Services.Portflio;

import com.google.common.collect.ImmutableMap;
import com.thomas.mockPortfolioManager.Models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PortfolioViewService {
    private final Logger LOGGER = LoggerFactory.getLogger(PortfolioViewService.class);
    private int historyCount = 1; //init a count for console output
    private final int DISPLAY_DP = 4;
    private PrintStream printStream = System.out;

    public PortfolioViewService(PrintStream printStream) {
        this.printStream = printStream;
    }

    public PortfolioViewService() {
    }

    public void processPortfolioUpdate(Map<Product, Map<String, String>> updateMap) {
        LOGGER.info("Starting to generate console output");
        BigDecimal totalPortfolioValue = BigDecimal.ZERO;
        Map<String, String> priceChangeMap = new HashMap<>();
        List<Map<String, String>> portfolioDetails = new ArrayList<>();

        for (Map.Entry<Product, Map<String, String>> update : updateMap.entrySet()) {
            Product product = update.getKey();
            Map<String, String> portfolioUpdate = update.getValue();
            if (!priceChangeMap.containsKey(update.getValue().get("underlying"))) {
                priceChangeMap.put(update.getValue().get("underlying"), new BigDecimal(update.getValue().get("newPrice")).setScale(DISPLAY_DP, RoundingMode.HALF_UP).toString());
            }
            totalPortfolioValue = totalPortfolioValue.add(new BigDecimal(update.getValue().get("totalPrice")));

            portfolioDetails.add(ImmutableMap.of("symbol", product.getSummary(),
                    "price", new BigDecimal(portfolioUpdate.get("price")).setScale(DISPLAY_DP, RoundingMode.HALF_UP).toString(),
                    "size", product.getSize().toString(),
                    "value", new BigDecimal(portfolioUpdate.get("totalPrice")).setScale(DISPLAY_DP, RoundingMode.HALF_UP).toString()));
        }

        printStream.printf("=============Market Data Update %s=============\n", historyCount);
        priceChangeMap.forEach((k, v) -> {
            printStream.printf("%s price change to %s\n", k, v);
        });
        printStream.printf("=============Portfolio=============\n");

        //headers
        printStream.printf("%-32s %32s %32s %32s\n", "Symbol", "Price", "Qty", "Value");

        //details
        //sort by symbol so ordering is consistent
        for (Map<String, String> details : portfolioDetails.stream().sorted((m1, m2) -> m1.get("symbol").compareTo(m2.get("symbol"))).collect(Collectors.toList())) {
            printStream.printf("%-32s %32s %32s %32s\n", details.get("symbol"), details.get("price"), details.get("size"), details.get("value"));
        }

        printStream.printf("%-98s %32s\n", "Total Portfolio Value", totalPortfolioValue.setScale(DISPLAY_DP, RoundingMode.HALF_UP));
        historyCount++;
    }
}
