package com.example.bikestore.bikestore.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bikestore.bikestore.Models.Order;
import com.example.bikestore.bikestore.Models.Product;
import com.example.bikestore.bikestore.Models.User;
import com.example.bikestore.bikestore.Repositories.OrderRepository;
import com.example.bikestore.bikestore.Repositories.ProductRepository;
import com.example.bikestore.bikestore.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @GetMapping("/orders")
    public Map<String, Object> getOrderDashboard() {
        List<Order> allOrders = orderRepository.findAll();
        long pendingCount = orderRepository.findByStatus("pending").size();
        long shippedCount = orderRepository.findByStatus("shipped").size();
        long deliveredCount = orderRepository.findByStatus("delivered").size();
        long cancelledCount = orderRepository.findByStatus("cancelled").size();

        Map<String, Object> response = new HashMap<>();
        response.put("totalOrders", allOrders.size());
        response.put("pending", pendingCount);
        response.put("shipped", shippedCount);
        response.put("delivered", deliveredCount);
        response.put("cancelled", cancelledCount);
        return response;
    }

    @GetMapping("/customers")
    public Map<String, Object> getCustomerDashboard() {
        List<User> allUsers = userRepository.findAll();
        long totalCustomers = allUsers.size();
        long customersWithOrders = orderRepository.findAll().stream()
                .map(Order::getUserId)
                .distinct()
                .count();

        Map<String, Object> response = new HashMap<>();
        response.put("totalCustomers", totalCustomers);
        response.put("activeCustomers", customersWithOrders);
        response.put("potentialCustomers", totalCustomers - customersWithOrders);
        return response;
    }

    @GetMapping("/revenue")
    public Map<String, Object> getRevenueDashboard() {
        List<Order> deliveredOrders = orderRepository.findByStatus("delivered");
        double totalRevenue = deliveredOrders.stream().mapToDouble(Order::getTotalPrice).sum();
        double averageOrderValue = deliveredOrders.isEmpty() ? 0 : totalRevenue / deliveredOrders.size();
        double conversionRate = orderRepository.findAll().isEmpty() ? 0 : 
                (double) deliveredOrders.size() / orderRepository.findAll().size() * 100;

        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", totalRevenue);
        response.put("averageOrderValue", averageOrderValue);
        response.put("conversionRate", conversionRate);
        return response;
    }

    @GetMapping("/sales-performance")
    public Map<String, Object> getSalesPerformanceDashboard() {
        List<Product> allProducts = productRepository.findAll();
        List<Product> topSelling = allProducts.stream()
                .filter(p -> p.getStock() < 5)
                .limit(5)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("totalProducts", allProducts.size());
        response.put("topSellingProducts", topSelling);
        response.put("lowStockProducts", allProducts.stream().filter(p -> p.getStock() == 0).count());
        return response;
    }

    @GetMapping("/marketing")
    public Map<String, Object> getMarketingDashboard() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalCampaigns", 5);
        response.put("totalCost", 50000000.0);
        response.put("roi", 150.0);
        return response;
    }

    @GetMapping("/finance")
    public Map<String, Object> getFinanceDashboard() {
        double totalRevenue = orderRepository.findByStatus("delivered").stream().mapToDouble(Order::getTotalPrice).sum();
        double operatingCost = 20000000.0;
        double profit = totalRevenue - operatingCost;

        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", totalRevenue);
        response.put("operatingCost", operatingCost);
        response.put("profit", profit);
        response.put("taxEstimate", profit * 0.1);
        return response;
    }

    @GetMapping("/products")
    public Map<String, Object> getProductManagementDashboard() {
        List<Product> products = productRepository.findAll();
        long outOfStock = products.stream().filter(p -> p.getStock() == 0).count();

        Map<String, Object> response = new HashMap<>();
        response.put("totalProducts", products.size());
        response.put("outOfStock", outOfStock);
        response.put("products", products);
        return response;
    }
}