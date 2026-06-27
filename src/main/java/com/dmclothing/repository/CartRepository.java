package com.dmclothing.repository;

import com.dmclothing.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByCustomerId(Long customerId);

    Optional<Cart> findByCustomerIdAndProductIdAndSelectedColorAndSelectedSize(
            Long customerId,
            Long productId,
            String selectedColor,
            String selectedSize
    );

    void deleteByCustomerId(Long customerId);
}