package com.example.bikestore.bikestore.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.bikestore.bikestore.Models.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByCustomerId(String customerId);
}