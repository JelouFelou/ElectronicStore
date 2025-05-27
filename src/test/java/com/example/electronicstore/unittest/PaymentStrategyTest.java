package com.example.electronicstore.unittest;

import com.example.electronicstore.entity.PaymentMethod;
import com.example.electronicstore.exception.PaymentProcessingException;
import com.example.electronicstore.strategy.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentStrategyTest {

    // --- Tests for each strategy ---
    @Test
    @DisplayName("CreditCard strategy should support CREDIT_CARD method")
    void creditCardStrategy_ShouldSupportCreditCard() {
        PaymentStrategy strategy = new CreditCardPaymentStrategy();
        assertEquals(PaymentMethod.CREDIT_CARD, strategy.getSupportedMethod());
    }

    @Test
    @DisplayName("PayPal strategy should support PAYPAL method")
    void paypalStrategy_ShouldSupportPayPal() {
        PaymentStrategy strategy = new PayPalPaymentStrategy();
        assertEquals(PaymentMethod.PAYPAL, strategy.getSupportedMethod());
    }

    @Test
    @DisplayName("Blik strategy should support BLIK method")
    void blikStrategy_ShouldSupportBlik() {
        PaymentStrategy strategy = new BlikPaymentStrategy();
        assertEquals(PaymentMethod.BLIK, strategy.getSupportedMethod());
    }

    @Test
    @DisplayName("BankTransfer strategy should support BANK_TRANSFER method")
    void bankTransferStrategy_ShouldSupportBankTransfer() {
        PaymentStrategy strategy = new BankTransferPaymentStrategy();
        assertEquals(PaymentMethod.BANK_TRANSFER, strategy.getSupportedMethod());
    }

    // --- Payment processing tests ---
    @Test
    @DisplayName("CreditCard payment should complete successfully")
    void creditCardPayment_ShouldCompleteSuccessfully() {
        PaymentStrategy strategy = new CreditCardPaymentStrategy();
        assertDoesNotThrow(() -> strategy.process(100.0));
    }

    @Test
    @DisplayName("PayPal payment should complete successfully")
    void paypalPayment_ShouldCompleteSuccessfully() {
        PaymentStrategy strategy = new PayPalPaymentStrategy();
        assertDoesNotThrow(() -> strategy.process(50.0));
    }

    @Test
    @DisplayName("BankTransfer payment should complete successfully")
    void bankTransferPayment_ShouldCompleteSuccessfully() {
        PaymentStrategy strategy = new BankTransferPaymentStrategy();
        assertDoesNotThrow(() -> strategy.process(200.0));
    }

    // --- Parameterized tests ---
    @ParameterizedTest
    @MethodSource("provideStrategiesForTesting")
    @DisplayName("All strategies should support their respective payment methods")
    void allStrategies_ShouldSupportCorrectMethods(PaymentStrategy strategy, PaymentMethod expectedMethod) {
        assertEquals(expectedMethod, strategy.getSupportedMethod());
    }

    private static Stream<Arguments> provideStrategiesForTesting() {
        return Stream.of(
                Arguments.of(new CreditCardPaymentStrategy(), PaymentMethod.CREDIT_CARD),
                Arguments.of(new PayPalPaymentStrategy(), PaymentMethod.PAYPAL),
                Arguments.of(new BlikPaymentStrategy(), PaymentMethod.BLIK),
                Arguments.of(new BankTransferPaymentStrategy(), PaymentMethod.BANK_TRANSFER)
        );
    }

    // --- Edge cases tests---
    @Test
    @DisplayName("Processing zero amount should not throw exception")
    void processZeroAmount_ShouldNotThrowException() {
        PaymentStrategy strategy = new CreditCardPaymentStrategy();
        assertDoesNotThrow(() -> strategy.process(0.0));
    }

    @Test
    @DisplayName("Processing negative amount should NOT throw exception (current implementation)")
    void processNegativeAmount_ShouldNotThrowException() {
        PaymentStrategy strategy = new CreditCardPaymentStrategy();
        assertDoesNotThrow(() -> strategy.process(-100.0));
    }
}