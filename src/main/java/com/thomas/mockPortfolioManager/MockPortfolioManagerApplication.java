package com.thomas.mockPortfolioManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MockPortfolioManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockPortfolioManagerApplication.class, args);
    }

}
