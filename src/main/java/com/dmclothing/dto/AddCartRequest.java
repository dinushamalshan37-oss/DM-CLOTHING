package com.dmclothing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartRequest {
    private Long customerId;
    private Long productId;
    private Integer quantity;
    private String selectedColor;
    private String selectedSize;
}