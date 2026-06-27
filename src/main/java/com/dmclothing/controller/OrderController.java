package com.dmclothing.controller;

import com.dmclothing.dto.CheckoutRequest;
import com.dmclothing.model.Order;
import com.dmclothing.model.OrderItem;
import com.dmclothing.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public String checkout(@RequestBody CheckoutRequest request) {
        return orderService.checkout(request);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getCustomerOrders(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItem> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }
}