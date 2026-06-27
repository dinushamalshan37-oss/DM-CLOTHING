package com.dmclothing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {
    private Long customerId;
    private String phone;
    private String city;
    private String address;
    private String latitude;
    private String longitude;
    private String paymentMethod;
}