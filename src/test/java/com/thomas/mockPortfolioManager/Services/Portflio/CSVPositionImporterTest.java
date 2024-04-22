package com.thomas.mockPortfolioManager.Services.Portflio;

import com.thomas.mockPortfolioManager.Enum.OptionType;
import com.thomas.mockPortfolioManager.Models.Instrument;
import com.thomas.mockPortfolioManager.Models.Product;
import com.thomas.mockPortfolioManager.Models.Stock;
import com.thomas.mockPortfolioManager.Models.Vanilla;
import com.thomas.mockPortfolioManager.Repositories.InstrumentRepository;
import com.thomas.mockPortfolioManager.Repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class CSVPositionImporterTest {
    @Mock
    private InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    @Mock
    private ProductRepository productRepository = mock(ProductRepository.class);
    @Captor
    private ArgumentCaptor<Instrument> instrumentArgumentCaptor = ArgumentCaptor.forClass(Instrument.class);
    @Captor
    private ArgumentCaptor<Iterable<Product>> productArgumentCaptor = ArgumentCaptor.forClass(Iterable.class);

    @Test
    public void testLoadStockFromCSV() {
        CSVPositionImporter csvPositionImporter = new CSVPositionImporter(instrumentRepository, productRepository, "testResources/testInputStock.csv");

        csvPositionImporter.loadPortfolioFromCSV();

        Mockito.verify(instrumentRepository).save(instrumentArgumentCaptor.capture());
        Mockito.verify(productRepository).saveAll(productArgumentCaptor.capture());

        Instrument instrument = instrumentArgumentCaptor.getValue();
        Product product = productArgumentCaptor.getValue().iterator().next();

        assertNotNull(instrument);
        assertNotNull(product);

        assertInstanceOf(Stock.class, instrument);
        assertEquals(instrument.getUnderlyingTicker(), "AAPL");
        assertEquals(product.getSize(), BigDecimal.valueOf(1000));
    }

    @Test
    public void testLoadVanillaFromCSV() {
        CSVPositionImporter csvPositionImporter = new CSVPositionImporter(instrumentRepository, productRepository, "testResources/testInputVanilla.csv");

        csvPositionImporter.loadPortfolioFromCSV();

        Mockito.verify(instrumentRepository).save(instrumentArgumentCaptor.capture());
        Mockito.verify(productRepository).saveAll(productArgumentCaptor.capture());

        Instrument instrument = instrumentArgumentCaptor.getValue();
        Product product = productArgumentCaptor.getValue().iterator().next();

        assertNotNull(instrument);
        assertNotNull(product);

        assertInstanceOf(Vanilla.class, instrument);
        Vanilla vanilla = (Vanilla) instrument;
        assertEquals(instrument.getUnderlyingTicker(), "AAPL");
        assertEquals(product.getSize(), BigDecimal.valueOf(-20000));

        assertEquals(vanilla.getMaturity(), LocalDate.of(2025, 10, 1));
        assertEquals(vanilla.getOptionType(), OptionType.C);
        assertEquals(vanilla.getStrike(), BigDecimal.valueOf(110));
    }
}