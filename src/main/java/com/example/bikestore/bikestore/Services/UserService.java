package com.example.bikestore.bikestore.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.bikestore.bikestore.Models.Order;
import com.example.bikestore.bikestore.Models.Transaction;
import com.example.bikestore.bikestore.Models.User;
import com.example.bikestore.bikestore.Repositories.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final TransactionService transactionService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User registerUser(@Valid User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }
        if (userRepository.existsByName(user.getName())) {
            throw new RuntimeException("Tên đăng nhập đã được sử dụng");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String email, String password) {
        // Tìm user theo email hoặc name (username)
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // Nếu không tìm thấy theo email, thử tìm theo name
            user = userRepository.findByName(email);
        }
        
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public User updateUser(String id, @Valid User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = optionalUser.get();
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        if (!userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setAddress(userDetails.getAddress());
        user.setPhone(userDetails.getPhone());
        user.setWishlist(userDetails.getWishlist());
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        userRepository.deleteById(id);
    }

    public void addToWishlist(String userId, String productId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = optionalUser.get();
        if (user.getWishlist() == null) {
            user.setWishlist(new ArrayList<>());
        }
        if (!user.getWishlist().contains(productId)) {
            user.getWishlist().add(productId);
            userRepository.save(user);
        }
    }

    public void removeFromWishlist(String userId, String productId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = optionalUser.get();
        if (user.getWishlist() != null) {
            user.getWishlist().remove(productId);
            userRepository.save(user);
        }
    }

    public List<Order> getUserOrders(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        return orderService.getOrdersByUserId(userId);
    }

    public List<Transaction> getUserTransactions(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        return transactionService.getTransactionsByCustomerId(userId);
    }
}