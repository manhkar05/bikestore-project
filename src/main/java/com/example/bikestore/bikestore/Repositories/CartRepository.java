package com.example.bikestore.bikestore.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.bikestore.bikestore.Models.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByUserId(String userId);
}