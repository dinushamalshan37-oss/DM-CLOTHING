package com.dmclothing.service;

import com.dmclothing.dto.AddCartRequest;
import com.dmclothing.model.Cart;
import com.dmclothing.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public String addToCart(AddCartRequest request) {
        Cart cart = cartRepository
                .findByCustomerIdAndProductIdAndSelectedColorAndSelectedSize(
                        request.getCustomerId(),
                        request.getProductId(),
                        request.getSelectedColor(),
                        request.getSelectedSize()
                )
                .orElse(null);

        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
            cartRepository.save(cart);
            return "Cart quantity updated";
        }

        Cart newCart = Cart.builder()
                .customerId(request.getCustomerId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .selectedColor(request.getSelectedColor())
                .selectedSize(request.getSelectedSize())
                .build();

        cartRepository.save(newCart);

        return "Product added to cart";
    }

    public List<Cart> getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    public String updateQuantity(Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.setQuantity(quantity);
        cartRepository.save(cart);

        return "Cart updated successfully";
    }

    public String removeCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
        return "Cart item removed";
    }

    @Transactional
    public String clearCart(Long customerId) {
        cartRepository.deleteByCustomerId(customerId);
        return "Cart cleared";
    }
}