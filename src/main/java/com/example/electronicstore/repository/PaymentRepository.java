package com.example.electronicstore.repository;

import com.example.electronicstore.entity.Payment;
import com.example.electronicstore.entity.PaymentMethod;
import com.example.electronicstore.entity.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    // Find payment by order ID
    Optional<Payment> findByOrderId(Long orderId);

    // Get payments by method
    List<Payment> findByMethod(PaymentMethod method);

    // Get payments by status
    List<Payment> findByStatus(PaymentStatus status);
}
