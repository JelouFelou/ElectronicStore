package com.example.electronicstore.repository;

import com.example.electronicstore.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    // Get all items from an order
    List<OrderItem> findByOrderId(Long orderId);
}
