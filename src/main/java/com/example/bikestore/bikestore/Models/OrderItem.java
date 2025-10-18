package com.example.bikestore.bikestore.Models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {
    @NotBlank(message = "ID sản phẩm không được để trống")
    private String productId;
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private int quantity;
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    private double price;
    // Optional snapshot fields to help the frontend display order details without extra lookups
    private String name;
    private String imageUrl;
}