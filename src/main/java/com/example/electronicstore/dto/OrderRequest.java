package com.example.electronicstore.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull(message = "Items list is required")
        @NotEmpty(message = "Order must contain at least one item")
        List<OrderItemRequest> items
) {}