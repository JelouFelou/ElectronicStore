package com.example.electronicstore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private double amount;
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
