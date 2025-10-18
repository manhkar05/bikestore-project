package com.example.bikestore.bikestore.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bikestore.bikestore.Models.Order;
import com.example.bikestore.bikestore.Models.OrderItem;
import com.example.bikestore.bikestore.Models.Product;
import com.example.bikestore.bikestore.Models.Transaction;
import com.example.bikestore.bikestore.Repositories.OrderRepository;
import com.example.bikestore.bikestore.Repositories.ProductRepository;
import com.example.bikestore.bikestore.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final TransactionService transactionService;

    public Order createOrder(String userId, List<OrderItem> items, String paymentMethod, String shippingAddress) {
        // Allow guest users (userId starting with "GUEST_")
        if (!userId.startsWith("GUEST_") && !userRepository.existsById(userId)) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        double totalPrice = 0;
        for (OrderItem item : items) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Sản phẩm không tồn tại");
            }
            Product product = productOpt.get();
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ hàng");
            }
            item.setPrice(product.getPrice());
            totalPrice += product.getPrice() * item.getQuantity();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setTotalPrice(totalPrice);
        order.setStatus("pending");
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(shippingAddress);
    Order savedOrder = orderRepository.save(order);
        
        // Create transaction record
        createTransaction(userId, items, totalPrice, savedOrder.getId());
        
        // Enrich saved order items with product snapshot (name, imageUrl)
        enrichOrderItemsWithProductInfo(savedOrder);

        return savedOrder;
    }

    public List<Order> getOrdersByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        orders.forEach(this::enrichOrderItemsWithProductInfo);
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(this::enrichOrderItemsWithProductInfo);
        return orders;
    }

    public Optional<Order> getOrderById(String id) {
        Optional<Order> optional = orderRepository.findById(id);
        optional.ifPresent(this::enrichOrderItemsWithProductInfo);
        return optional;
    }

    // Helper to attach product name and imageUrl to each order item when possible
    private void enrichOrderItemsWithProductInfo(Order order) {
        if (order == null || order.getItems() == null) return;
        for (OrderItem item : order.getItems()) {
            try {
                productRepository.findById(item.getProductId()).ifPresent(product -> {
                    item.setName(product.getName());
                    item.setImageUrl(product.getImageUrl());
                });
            } catch (Exception e) {
                // ignore enrichment failures
            }
        }
    }

    public Order updateOrderStatus(String id, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Đơn hàng không tồn tại");
        }
        Order order = optionalOrder.get();
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Đơn hàng không tồn tại");
        }
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
    
    private void createTransaction(String userId, List<OrderItem> items, double totalAmount, String orderId) {
        try {
            Transaction transaction = new Transaction();
            transaction.setCustomerId(userId);
            transaction.setTotalAmount(totalAmount);
            transaction.setStatus("completed");
            transaction.setCreatedAt(java.time.Instant.now());
            
            // Convert OrderItem to Transaction.ProductItem
            List<Transaction.ProductItem> productItems = items.stream()
                .map(item -> {
                    Transaction.ProductItem productItem = new Transaction.ProductItem();
                    productItem.setProductId(item.getProductId());
                    productItem.setQuantity(item.getQuantity());
                    return productItem;
                })
                .collect(java.util.stream.Collectors.toList());
            
            transaction.setProducts(productItems);
            
            transactionService.createTransaction(transaction);
        } catch (Exception e) {
            // Log error but don't fail the order creation
            System.err.println("Failed to create transaction for order " + orderId + ": " + e.getMessage());
        }
    }
}