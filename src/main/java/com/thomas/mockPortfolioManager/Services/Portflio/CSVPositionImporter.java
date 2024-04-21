package com.thomas.mockPortfolioManager.Services.Portflio;

import com.thomas.mockPortfolioManager.Enum.OptionType;
import com.thomas.mockPortfolioManager.Models.Instrument;
import com.thomas.mockPortfolioManager.Models.Product;
import com.thomas.mockPortfolioManager.Models.Stock;
import com.thomas.mockPortfolioManager.Models.Vanilla;
import com.thomas.mockPortfolioManager.Repositories.InstrumentRepository;
import com.thomas.mockPortfolioManager.Repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ManagedResource
@Service
public class CSVPositionImporter {

    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Value("${portfolio.import.file.path:}")
    private String filePath;
    private final Logger LOGGER = LoggerFactory.getLogger(CSVPositionImporter.class);

    private final DateTimeFormatter vanillaMaturityFormat = new DateTimeFormatterBuilder().parseDefaulting(ChronoField.DAY_OF_MONTH,1).parseCaseInsensitive().appendPattern("MMMyyyy").toFormatter();

    @PostConstruct
    public void init() {
        LOGGER.info("Init load from CSV");
        loadPortfolioFromCSV();
    }

    @ManagedOperation
    public void loadPortfolioFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new ClassPathResource(filePath).getFile()))) {
            List<Product> productList = new ArrayList<>();
            String line;
            while (!StringUtils.isEmpty(line = reader.readLine())) {
                Product product = parseLineToProduct(line.trim());
                if(Objects.nonNull(product)){
                    productList.add(product);
                }
            }
            productRepository.saveAll(productList);
        } catch (Exception exception) {
            LOGGER.error("Failed to initialise portfolio", exception);
        }
    }

    private Product parseLineToProduct(String input) {
        String[] inputArray = StringUtils.commaDelimitedListToStringArray(input);
        if (inputArray.length < 2) {
            //not a valid input
            LOGGER.error("Failed to load product with input: {}", input);
            return null;
        }
        String ticker = inputArray[0]; //todo check against market data to see if instrument is valid
        BigDecimal size;
        try {
            size = new BigDecimal(inputArray[1]);
        } catch (NumberFormatException numberFormatException) {
            LOGGER.error("Invalid size format", numberFormatException);
            return null;
        }
        Product product = new Product(size, ticker);
        if (isStock(ticker)) {
            product.setInstrument(fetchOrCreateStock(inputArray[0]));
            return product;
        } else {
            Vanilla vanilla = fetchOrCreateVanilla(inputArray[0]);
            if (Objects.nonNull(vanilla)) {
                product.setInstrument(vanilla);
                return product;
            }
        }
        return null;
    }

    private Stock fetchOrCreateStock(String ticker) {
        Optional<Instrument> optionalStock = instrumentRepository.findByTicker(ticker);
        if (optionalStock.isPresent() && optionalStock.get() instanceof Stock) {
            return (Stock) optionalStock.get();
        }else{
            Stock stock = new Stock(ticker);
            instrumentRepository.save(stock);
            return stock;
        }
    }

    private Vanilla fetchOrCreateVanilla(String ticker) {
        Optional<Instrument> optionalVanilla = instrumentRepository.findByTicker(ticker);
        if (optionalVanilla.isPresent() && optionalVanilla.get() instanceof Vanilla) {
            return (Vanilla) optionalVanilla.get();
        }
        String[] vanillaInput = StringUtils.delimitedListToStringArray(ticker, "-");
        if (vanillaInput.length < 5) {// we are expecting at least 5 components for the economics
            LOGGER.error("Invalid vanilla ticker input for {}", ticker);
            return null;
        }
        LocalDate maturity;
        try {
            maturity = LocalDate.parse(vanillaInput[1] + vanillaInput[2], vanillaMaturityFormat);
        } catch (DateTimeException dateTimeException) {
            LOGGER.error("Invalid vanilla maturity input for {}", ticker);
            return null;
        }

        BigDecimal strike;
        try {
            strike = new BigDecimal(vanillaInput[3]);
        } catch (NumberFormatException numberFormatException) {
            LOGGER.error("Invalid vanilla strike input for {}", ticker);
            return null;
        }

        OptionType optionType;
        try {
            optionType = OptionType.valueOf(vanillaInput[4]);
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.error("Invalid vanilla option type input for {}", ticker);
            return null;
        }

        String underlying = vanillaInput[0];

        Vanilla vanilla = new Vanilla(ticker, maturity, optionType, strike, underlying);
        instrumentRepository.save(vanilla);
        return vanilla;
    }

    private boolean isStock(String ticker) { //is stock if it does not contain dashes (-)
        return !ticker.contains("-");
    }

}
