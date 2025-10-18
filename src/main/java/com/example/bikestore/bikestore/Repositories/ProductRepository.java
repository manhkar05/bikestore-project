package com.example.bikestore.bikestore.Repositories;

import com.example.bikestore.bikestore.Models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategory(String category);
    List<Product> findByBrand(String brand);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}