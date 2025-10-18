package com.example.bikestore.bikestore.Models;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String productId;
    private String userId;
    private int rating; // 1-5
    private String comment;
    private Instant createdAt;
}