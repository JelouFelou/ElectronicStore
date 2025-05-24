package com.example.electronicstore.dto;

import com.example.electronicstore.entity.PaymentMethod;
import com.example.electronicstore.entity.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        PaymentMethod method,
        double amount,
        PaymentStatus status,
        LocalDateTime paymentDate
) {}