package com.example.bikestore.bikestore.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;
    private String category;
    private String brand;
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    private double price;
    private String description;
    private String imageUrl;
    @Min(value = 0, message = "Tồn kho phải lớn hơn hoặc bằng 0")
    private int stock;
}