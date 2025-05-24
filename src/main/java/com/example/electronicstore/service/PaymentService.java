package com.example.electronicstore.service;

import com.example.electronicstore.dto.PaymentResponse;
import com.example.electronicstore.entity.*;
import com.example.electronicstore.exception.PaymentProcessingException;
import com.example.electronicstore.exception.ResourceNotFoundException;
import com.example.electronicstore.repository.PaymentRepository;
import com.example.electronicstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // Poprawna sygnatura: Long orderId zamiast Order
    public PaymentResponse processPayment(Long orderId, PaymentMethod method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());

        try {
            Thread.sleep(1000);
            payment.setStatus(PaymentStatus.COMPLETED);
            order.setStatus(OrderStatus.PAID);
        } catch (InterruptedException e) {
            payment.setStatus(PaymentStatus.FAILED);
            throw new PaymentProcessingException("Payment processing interrupted");
        }

        orderRepository.save(order);
        Payment savedPayment = paymentRepository.save(payment);
        return convertToResponse(savedPayment);
    }

    private PaymentResponse convertToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getMethod(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentDate()
        );
    }
}