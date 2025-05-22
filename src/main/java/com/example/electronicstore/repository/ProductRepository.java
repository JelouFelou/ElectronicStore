package com.example.electronicstore.repository;

import com.example.electronicstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Search products by name (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Get products by category
    List<Product> findByCategory(String category);

    // Get low-stock products (< 5 items)
    List<Product> findByStockLessThan(int threshold);

    // Get products in price range
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}
