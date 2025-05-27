package com.example.electronicstore.strategy;

import com.example.electronicstore.entity.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.CREDIT_CARD;
    }

    @Override
    public boolean process(double amount) {
        return true;
    }
}