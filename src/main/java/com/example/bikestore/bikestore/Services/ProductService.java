package com.example.bikestore.bikestore.Services;

import com.example.bikestore.bikestore.Models.Product;
import com.example.bikestore.bikestore.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public Product saveProduct(@Valid Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(String id, @Valid Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }
        Product product = optionalProduct.get();
        product.setName(productDetails.getName());
        product.setCategory(productDetails.getCategory());
        product.setBrand(productDetails.getBrand());
        product.setPrice(productDetails.getPrice());
        product.setDescription(productDetails.getDescription());
        product.setImageUrl(productDetails.getImageUrl());
        product.setStock(productDetails.getStock());
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }
        productRepository.deleteById(id);
    }
}