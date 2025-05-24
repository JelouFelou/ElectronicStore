package com.example.electronicstore.controller;

import com.example.electronicstore.dto.OrderRequest;
import com.example.electronicstore.dto.OrderResponse;
import com.example.electronicstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "Order management endpoints")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create new order", description = "Creates a new order for authenticated user")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> createOrder(
            @Parameter(description = "Order creation data", required = true)
            @Valid @RequestBody OrderRequest request,
            @AuthenticationPrincipal String username
    ) {
        OrderResponse response = orderService.createOrder(request, username);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get order details", description = "Retrieves order by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}