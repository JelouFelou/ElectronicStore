package com.example.electronicstore.dto;

public record ProductResponse(
        Long id,
        String name,
        double price,
        int stock,
        String category
) {}