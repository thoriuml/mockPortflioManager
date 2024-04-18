package com.thomas.mockPortfolioManager.Repositories;

import com.thomas.mockPortfolioManager.Models.Instrument;
import com.thomas.mockPortfolioManager.Models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstrumentRepository extends CrudRepository<Instrument, Long> {
    Optional<Instrument> findByTicker(String ticker);

}
