package com.example.electronicstore.controller;

import com.example.electronicstore.entity.*;
import com.example.electronicstore.service.OrderService;
import com.example.electronicstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    @PostMapping("/{orderId}")
    public ResponseEntity<Payment> processPayment(
            @PathVariable Long orderId,
            @RequestParam PaymentMethod method
    ) {
        Order order = orderService.getOrderById(orderId);
        Payment payment = paymentService.processPayment(order, method);
        return ResponseEntity.ok(payment);
    }
}