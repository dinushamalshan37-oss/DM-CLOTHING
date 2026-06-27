package com.dmclothing.controller;

import com.dmclothing.dto.LoginRequest;
import com.dmclothing.dto.OwnerLoginRequest;
import com.dmclothing.dto.RegisterRequest;
import com.dmclothing.dto.VerifyOtpRequest;
import com.dmclothing.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.dmclothing.dto.OwnerRegisterRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody VerifyOtpRequest request) {
        return authService.verifyOtp(request);
    }

    @PostMapping("/login")
    public String customerLogin(@RequestBody LoginRequest request) {
        return authService.customerLogin(request);
    }

    @PostMapping("/owner-login")
    public String ownerLogin(@RequestBody OwnerLoginRequest request) {
        return authService.ownerLogin(request);
    }
    @PostMapping("/owner-register")
public String ownerRegister(@RequestBody OwnerRegisterRequest request) {
    return authService.ownerRegister(request);
}
}