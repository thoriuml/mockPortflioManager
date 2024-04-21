package com.thomas.mockPortfolioManager.Services.Portflio;

import com.google.common.collect.ImmutableMap;
import com.thomas.mockPortfolioManager.Models.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PortfolioViewService {

    private int historyCount = 1; //init a count for console output

    private final int DISPLAY_DP = 4;

    public void processPortfolioUpdate(Map<Product, Map<String, String>> updateMap) {
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

        System.out.printf("=============Market Data Update %s=============\n", historyCount);
        priceChangeMap.forEach((k, v) -> {
            System.out.printf("%s price change to %s\n", k, v);
        });
        System.out.print("=============Portfolio=============\n");

        //headers
        System.out.printf("%-32s %32s %32s %32s\n", "Symbol", "Price", "Qty", "Value");

        //details
        //sort by symbol so ordering is consistent
        for (Map<String, String> details : portfolioDetails.stream().sorted((m1, m2) -> m1.get("symbol").compareTo(m2.get("symbol"))).collect(Collectors.toList())) {
            System.out.printf("%-32s %32s %32s %32s\n", details.get("symbol"), details.get("price"), details.get("size"), details.get("value"));
        }

        System.out.printf("%-98s %32s\n", "Total Portfolio Value", totalPortfolioValue.setScale(DISPLAY_DP, RoundingMode.HALF_UP));
        historyCount++;
    }
}
