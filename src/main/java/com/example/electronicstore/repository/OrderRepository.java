package com.example.electronicstore.repository;

import com.example.electronicstore.entity.Order;
import com.example.electronicstore.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Get user's orders
    List<Order> findByUserId(Long userId);

    // Get orders by status
    List<Order> findByStatus(OrderStatus status);

    // Get orders from date range
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    // Get high-value orders (amount > X)
    List<Order> findByTotalAmountGreaterThan(double amount);
}
