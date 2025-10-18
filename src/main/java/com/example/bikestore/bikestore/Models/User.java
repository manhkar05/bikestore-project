package com.example.bikestore.bikestore.Models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    private String address;
    private String phone;
    // role: 'admin' or 'customer'
    private String role = "customer";
    private List<String> wishlist = new ArrayList<>();
}