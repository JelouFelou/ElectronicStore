package com.example.electronicstore.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String status,
        LocalDateTime createdAt,
        double totalAmount,
        List<OrderItemResponse> items
) {}