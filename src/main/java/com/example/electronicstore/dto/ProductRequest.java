package com.example.electronicstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank(message = "Product name is required")
        String name,

        @Positive(message = "Price must be positive")
        double price,

        @Min(value = 0, message = "Stock cannot be negative")
        int stock,

        String category
) {}