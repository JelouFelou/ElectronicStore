package com.example.electronicstore.service;

import com.example.electronicstore.entity.*;
import com.example.electronicstore.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.electronicstore.repository.OrderRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public Payment processPayment(Order order, PaymentMethod method) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());

        // Simulate payment processing
        try {
            Thread.sleep(1000); // Simulate processing time

            payment.setStatus(PaymentStatus.COMPLETED);
            order.setStatus(OrderStatus.PAID);

            orderRepository.save(order);
            return paymentRepository.save(payment);

        } catch (InterruptedException e) {
            payment.setStatus(PaymentStatus.FAILED);
            return paymentRepository.save(payment);
        }
    }
}
