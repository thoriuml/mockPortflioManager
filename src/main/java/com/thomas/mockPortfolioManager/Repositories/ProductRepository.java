package com.thomas.mockPortfolioManager.Repositories;

import com.thomas.mockPortfolioManager.Models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}
