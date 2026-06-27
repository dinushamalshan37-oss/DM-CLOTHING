package com.dmclothing.repository;

import com.dmclothing.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByOrderStatus(String orderStatus);

    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}