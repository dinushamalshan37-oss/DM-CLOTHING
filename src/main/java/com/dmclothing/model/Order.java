package com.dmclothing.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long customerId;

    private BigDecimal totalAmount;

    private String paymentMethod;

    @Column(columnDefinition = "TEXT")
    private String deliveryAddress;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String city;

    private String phone;

    private String latitude;

    private String longitude;

    private String orderStatus;

    private LocalDateTime orderDate;

    @PrePersist
    public void onCreate() {
        orderDate = LocalDateTime.now();

        if (orderStatus == null) {
            orderStatus = "Pending";
        }
    }
}