package com.example.bikestore.bikestore.Models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    private String id;
    @NotBlank(message = "ID người dùng không được để trống")
    private String userId;
    private List<OrderItem> items;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String paymentMethod;
    private String shippingAddress;
}