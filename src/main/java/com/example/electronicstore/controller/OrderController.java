package com.example.electronicstore.controller;

import com.example.electronicstore.entity.*;
import com.example.electronicstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderService.OrderCreateDto dto,
            @AuthenticationPrincipal String username
    ) {
        Order order = orderService.createOrder(dto, username);
        return ResponseEntity.ok(order);
    }
}