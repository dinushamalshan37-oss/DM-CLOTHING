package com.dmclothing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerRegisterRequest {
    private String username;
    private String password;
}