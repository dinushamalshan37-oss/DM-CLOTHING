package com.dmclothing.service;

import com.dmclothing.dto.CheckoutRequest;
import com.dmclothing.model.Cart;
import com.dmclothing.model.Customer;
import com.dmclothing.model.Order;
import com.dmclothing.model.OrderItem;
import com.dmclothing.model.Product;
import com.dmclothing.repository.CartRepository;
import com.dmclothing.repository.CustomerRepository;
import com.dmclothing.repository.OrderItemRepository;
import com.dmclothing.repository.OrderRepository;
import com.dmclothing.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    @Transactional
    public String checkout(CheckoutRequest request) {
        List<Cart> cartItems = cartRepository.findByCustomerId(request.getCustomerId());

        if (cartItems.isEmpty()) {
            return "Cart is empty";
        }

        BigDecimal total = BigDecimal.ZERO;

        for (Cart cart : cartItems) {
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
            total = total.add(subtotal);
        }

        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .phone(request.getPhone())
                .city(request.getCity())
                .address(request.getAddress())
                .deliveryAddress(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .paymentMethod(request.getPaymentMethod())
                .totalAmount(total)
                .orderStatus("Pending")
                .build();

        Order savedOrder = orderRepository.save(order);

        for (Cart cart : cartItems) {
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .orderId(savedOrder.getOrderId())
                    .productId(product.getProductId())
                    .quantity(cart.getQuantity())
                    .selectedColor(cart.getSelectedColor())
                    .selectedSize(cart.getSelectedSize())
                    .price(product.getPrice())
                    .subtotal(subtotal)
                    .build();

            orderItemRepository.save(orderItem);
        }

        cartRepository.deleteByCustomerId(request.getCustomerId());

        return "Order placed successfully. Order ID: " + savedOrder.getOrderId();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public String updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status);
        orderRepository.save(order);

        Customer customer = customerRepository.findById(order.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        emailService.sendOrderStatusEmail(
                customer.getEmail(),
                customer.getName(),
                orderId,
                status
        );

        return "Order status updated to " + status;
    }
    public List<Order> getOrdersByCustomerId(Long customerId) {
    return orderRepository.findByCustomerId(customerId);
}
public BigDecimal getMonthlySales() {
    LocalDate now = LocalDate.now();

    LocalDateTime start = now.withDayOfMonth(1).atStartOfDay();
    LocalDateTime end = now.plusMonths(1).withDayOfMonth(1).atStartOfDay();

    List<Order> monthlyOrders = orderRepository.findByOrderDateBetween(start, end);

    BigDecimal total = BigDecimal.ZERO;

    for (Order order : monthlyOrders) {
        if (order.getTotalAmount() != null) {
            total = total.add(order.getTotalAmount());
        }
    }

    return total;
}

public Map<String, Object> getDashboardStats() {
    Map<String, Object> stats = new HashMap<>();

    stats.put("monthlySales", getMonthlySales());
    stats.put("totalOrders", orderRepository.count());
    stats.put("pendingOrders", orderRepository.findByOrderStatus("Pending").size());
    stats.put("processingOrders", orderRepository.findByOrderStatus("Processing").size());
    stats.put("deliveryOrders", orderRepository.findByOrderStatus("Delivery").size());
    stats.put("completedOrders", orderRepository.findByOrderStatus("Completed").size());
    stats.put("lowStockCount", productRepository.findByStockLessThanEqual(5).size());

    return stats;
}

public List<Map<String, Object>> getAllOrdersWithCustomerDetails() {
    List<Order> orders = orderRepository.findAll();

    return orders.stream().map(order -> {
        Map<String, Object> data = new HashMap<>();

        Customer customer = customerRepository.findById(order.getCustomerId()).orElse(null);
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getOrderId());

        data.put("order", order);
        data.put("items", items);

        if (customer != null) {
            data.put("customerName", customer.getName());
            data.put("customerEmail", customer.getEmail());
            data.put("customerPhone", customer.getPhone());
        } else {
            data.put("customerName", "Unknown");
            data.put("customerEmail", "");
            data.put("customerPhone", "");
        }

        return data;
    }).toList();
}
}