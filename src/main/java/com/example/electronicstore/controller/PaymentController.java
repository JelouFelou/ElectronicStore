package com.example.electronicstore.controller;

import com.example.electronicstore.dto.PaymentRequest;
import com.example.electronicstore.dto.PaymentResponse;
import com.example.electronicstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payments", description = "Payment processing endpoints")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Process a payment",
            description = "Processes a payment for an existing order. " +
                    "The user must be the owner of the order (provided in the 'X-Username' header)."
    )
    @PostMapping
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> processPayment(
            @Parameter(description = "Payment request data", required = true)
            @Valid @RequestBody PaymentRequest request
    ) {
        PaymentResponse response = paymentService.processPayment(
                request.orderId(),
                request.method()
        );
        return ResponseEntity.ok(response);
    }
}