package com.dmclothing.controller;

import com.dmclothing.dto.OrderStatusRequest;
import com.dmclothing.model.Order;
import com.dmclothing.model.OrderItem;
import com.dmclothing.model.Product;
import com.dmclothing.service.AuthService;
import com.dmclothing.service.OrderService;
import com.dmclothing.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OwnerController {

    private final AuthService authService;
    private final ProductService productService;
    private final OrderService orderService;

    @PostMapping("/create-default")
    public String createDefaultOwner() {
        return authService.createDefaultOwner();
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("/products/{productId}")
    public Product updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        return productService.updateProduct(productId, product);
    }

    @DeleteMapping("/products/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{orderId}/items")
    public List<OrderItem> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @PutMapping("/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusRequest request) {
        return orderService.updateOrderStatus(orderId, request.getOrderStatus());
    }
    @GetMapping("/dashboard-stats")
public Map<String, Object> getDashboardStats() {
    return orderService.getDashboardStats();
}

@GetMapping("/low-stock")
public List<Product> getLowStockProducts() {
    return productService.getLowStockProducts();
}

@GetMapping("/orders-details")
public List<Map<String, Object>> getOrdersWithCustomerDetails() {
    return orderService.getAllOrdersWithCustomerDetails();
}
}