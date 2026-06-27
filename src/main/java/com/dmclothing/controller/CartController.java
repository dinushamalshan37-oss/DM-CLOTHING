package com.dmclothing.controller;

import com.dmclothing.dto.AddCartRequest;
import com.dmclothing.model.Cart;
import com.dmclothing.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestBody AddCartRequest request) {
        return cartService.addToCart(request);
    }

    @GetMapping("/customer/{customerId}")
    public List<Cart> getCartByCustomerId(@PathVariable Long customerId) {
        return cartService.getCartByCustomerId(customerId);
    }

    @PutMapping("/{cartId}/quantity/{quantity}")
    public String updateQuantity(@PathVariable Long cartId, @PathVariable Integer quantity) {
        return cartService.updateQuantity(cartId, quantity);
    }

    @DeleteMapping("/{cartId}")
    public String removeCartItem(@PathVariable Long cartId) {
        return cartService.removeCartItem(cartId);
    }

    @DeleteMapping("/clear/{customerId}")
    public String clearCart(@PathVariable Long customerId) {
        return cartService.clearCart(customerId);
    }
}