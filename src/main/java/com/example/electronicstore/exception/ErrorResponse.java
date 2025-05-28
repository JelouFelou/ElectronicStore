package com.example.electronicstore.exception;

public record ErrorResponse(
        int status,
        String message,
        long timestamp
) {}