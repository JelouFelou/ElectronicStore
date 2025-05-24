package com.example.electronicstore.dto;

public record UserResponse(
        Long id,
        String username,
        String email,
        String role
) {}