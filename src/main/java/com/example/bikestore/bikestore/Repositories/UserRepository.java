package com.example.bikestore.bikestore.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.bikestore.bikestore.Models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    User findByName(String name);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}