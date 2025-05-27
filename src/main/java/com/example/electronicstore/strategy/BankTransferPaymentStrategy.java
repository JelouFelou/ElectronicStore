package com.example.electronicstore.strategy;

import com.example.electronicstore.entity.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class BankTransferPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.BANK_TRANSFER;
    }

    @Override
    public boolean process(double amount) {
        return true;
    }
}