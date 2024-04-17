package com.thomas.mockPortfolioManager.conf;

import com.thomas.mockPortfolioManager.Services.PortfolioImporter.CSVPositionImporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortfolioManagerConfiguration {

    @Bean
    public CSVPositionImporter csvPositionParser(){
        return new CSVPositionImporter();
    }
}
