package com.example.bikestore.bikestore.Models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String userId;
    private List<CartItem> items;

    @Data
    public static class CartItem {
        private String id;           // Product ID (for compatibility with frontend)
        private String productId;    // Product ID (backend standard)
        private String name;         // Product name
        private double price;        // Product price
        private String image;        // Product image URL
        private int quantity;        // Quantity in cart
        
        // Constructor for backward compatibility
        public CartItem() {}
        
        // Constructor for frontend compatibility
        public CartItem(String id, String name, double price, String image, int quantity) {
            this.id = id;
            this.productId = id;  // Set both for compatibility
            this.name = name;
            this.price = price;
            this.image = image;
            this.quantity = quantity;
        }
    }
}