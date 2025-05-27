package com.example.electronicstore.service;

import com.example.electronicstore.dto.PaymentResponse;
import com.example.electronicstore.entity.*;
import com.example.electronicstore.exception.PaymentProcessingException;
import com.example.electronicstore.exception.ResourceNotFoundException;
import com.example.electronicstore.repository.PaymentRepository;
import com.example.electronicstore.repository.OrderRepository;
import com.example.electronicstore.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final List<PaymentStrategy> paymentStrategies;

    public PaymentResponse processPayment(Long orderId, PaymentMethod method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        PaymentStrategy strategy = paymentStrategies.stream()
                .filter(s -> s.getSupportedMethod() == method)
                .findFirst()
                .orElseThrow(() -> new PaymentProcessingException("Unsupported payment method"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(method);
        payment.setPaymentDate(LocalDateTime.now());

        try {
            strategy.process(order.getTotalAmount());
            payment.setStatus(PaymentStatus.COMPLETED);
            order.setStatus(OrderStatus.PAID);
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            throw new PaymentProcessingException("Payment failed: " + e.getMessage());
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