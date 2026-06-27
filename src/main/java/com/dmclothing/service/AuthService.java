package com.dmclothing.service;

import com.dmclothing.dto.LoginRequest;
import com.dmclothing.dto.OwnerLoginRequest;
import com.dmclothing.dto.RegisterRequest;
import com.dmclothing.dto.VerifyOtpRequest;
import com.dmclothing.model.Customer;
import com.dmclothing.model.Owner;
import com.dmclothing.repository.CustomerRepository;
import com.dmclothing.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.dmclothing.dto.OwnerRegisterRequest;
import com.dmclothing.model.Owner;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final EmailService emailService;

    public String register(RegisterRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            return "Email already registered";
        }

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .verified(false)
                .otp(otp)
                .build();

        customerRepository.save(customer);

        emailService.sendOtpEmail(request.getEmail(), otp);

        return "Registration successful. OTP sent to email.";
    }

    public String verifyOtp(VerifyOtpRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.getOtp().equals(request.getOtp())) {
            return "Invalid OTP";
        }

        customer.setVerified(true);
        customer.setOtp(null);

        customerRepository.save(customer);

        return "Account verified successfully";
    }

    public String customerLogin(LoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.isVerified()) {
            return "Please verify your account first";
        }

        if (!customer.getPassword().equals(request.getPassword())) {
            return "Invalid password";
        }

        return "Customer login successful. Customer ID: " + customer.getCustomerId();
    }

    public String ownerLogin(OwnerLoginRequest request) {
        Owner owner = ownerRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (!owner.getPassword().equals(request.getPassword())) {
            return "Invalid owner password";
        }

        return "Owner login successful. Owner ID: " + owner.getOwnerId();
    }

    public String createDefaultOwner() {
        if (ownerRepository.findByUsername("admin").isPresent()) {
            return "Owner already exists";
        }

        Owner owner = Owner.builder()
                .username("admin")
                .password("admin123")
                .build();

        ownerRepository.save(owner);

        return "Default owner created. Username: admin Password: admin123";
    }
    public String ownerRegister(OwnerRegisterRequest request) {
    if (ownerRepository.findByUsername(request.getUsername()).isPresent()) {
        return "Owner username already exists";
    }

    Owner owner = Owner.builder()
            .username(request.getUsername())
            .password(request.getPassword())
            .build();

    ownerRepository.save(owner);

    return "Owner registered successfully";
}
}