package com.example.electronicstore.dto;

public record OrderItemResponse(
        Long productId,
        String productName,
        int quantity,
        double unitPrice
) {}
