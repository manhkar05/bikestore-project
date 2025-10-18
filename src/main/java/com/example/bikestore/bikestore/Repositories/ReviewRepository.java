package com.example.bikestore.bikestore.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.bikestore.bikestore.Models.Review;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByProductId(String productId);
}