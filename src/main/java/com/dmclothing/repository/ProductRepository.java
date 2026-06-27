package com.dmclothing.repository;

import com.dmclothing.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByStockLessThanEqual(Integer stock);
}