package com.dmclothing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerLoginRequest {
    private String username;
    private String password;
}