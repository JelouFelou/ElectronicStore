package com.example.electronicstore.strategy;

import com.example.electronicstore.entity.PaymentMethod;

public interface PaymentStrategy {
    PaymentMethod getSupportedMethod(); // Returns Payment Method
    boolean process(double amount); // Returns TRUE if payment succeeded
}