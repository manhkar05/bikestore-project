package com.example.bikestore.bikestore.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bikestore.bikestore.Models.Cart;
import com.example.bikestore.bikestore.Repositories.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            // Create empty cart if not exists
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
        }
        return cart;
    }

    public Cart saveCart(Cart cart) {
        // If cart has no ID, it's a new cart
        if (cart.getId() == null) {
            // Check if cart already exists for this user
            Cart existingCart = cartRepository.findByUserId(cart.getUserId());
            if (existingCart != null) {
                // Update existing cart
                existingCart.setItems(cart.getItems());
                return cartRepository.save(existingCart);
            }
        }
        return cartRepository.save(cart);
    }

    public void deleteCart(String id) {
        cartRepository.deleteById(id);
    }
}