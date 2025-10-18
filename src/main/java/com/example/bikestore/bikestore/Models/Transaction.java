package com.example.bikestore.bikestore.Models;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String customerId;
    private List<ProductItem> products;
    private double totalAmount;
    private String status;
    private Instant createdAt;

    @Data
    public static class ProductItem {
        private String productId;
        private int quantity;
    }
}