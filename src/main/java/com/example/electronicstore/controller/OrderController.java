package com.example.electronicstore.controller;

import com.example.electronicstore.dto.OrderRequest;
import com.example.electronicstore.dto.OrderResponse;
import com.example.electronicstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Orders", description = "Order management endpoints")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order for the specified user. " +
                    "The user is identified by the 'X-Username' header. " +
                    "The order must include at least one item."
    )
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> createOrder(
            @Valid @RequestBody OrderRequest request,
            @RequestHeader("X-Username") String username
    ) {
        Map<String, Object> response = orderService.createOrder(request, username);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get order details",
            description = "Retrieves the details of an order by its ID."
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}