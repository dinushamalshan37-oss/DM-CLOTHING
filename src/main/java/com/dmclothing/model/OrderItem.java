package com.dmclothing.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    private Long orderId;

    private Long productId;

    private Integer quantity;

    private String selectedColor;

    private String selectedSize;

    private BigDecimal price;

    private BigDecimal subtotal;
}