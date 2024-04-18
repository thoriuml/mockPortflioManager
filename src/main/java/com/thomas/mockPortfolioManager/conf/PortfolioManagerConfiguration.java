package com.thomas.mockPortfolioManager.conf;

import com.thomas.mockPortfolioManager.Repositories.InstrumentRepository;
import com.thomas.mockPortfolioManager.Repositories.ProductRepository;
import com.thomas.mockPortfolioManager.Services.PortfolioImporter.CSVPositionImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortfolioManagerConfiguration {

    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private ProductRepository productRepository;

    @Bean
    public CSVPositionImporter CSVPositionImporter(ProductRepository productRepository, InstrumentRepository instrumentRepository){
        return new CSVPositionImporter(productRepository, instrumentRepository);
    }
}
