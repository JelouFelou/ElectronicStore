package com.example.electronicstore.unittest.service;

import com.example.electronicstore.dto.PaymentResponse;
import com.example.electronicstore.entity.*;
import com.example.electronicstore.exception.PaymentProcessingException;
import com.example.electronicstore.exception.ResourceNotFoundException;
import com.example.electronicstore.repository.OrderRepository;
import com.example.electronicstore.repository.PaymentRepository;
import com.example.electronicstore.service.PaymentService;
import com.example.electronicstore.strategy.CreditCardPaymentStrategy;
import com.example.electronicstore.strategy.PaymentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private List<PaymentStrategy> paymentStrategies;

    @InjectMocks
    private PaymentService paymentService;

    private Order order;
    private PaymentStrategy creditCardStrategy;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setTotalAmount(100.0);
        order.setStatus(OrderStatus.PENDING);

        creditCardStrategy = new CreditCardPaymentStrategy();
    }

    @Test
    @DisplayName("Should process credit card payment successfully and return COMPLETED status")
    void processPayment_Success() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentStrategies.stream()).thenReturn(Collections.singletonList(creditCardStrategy).stream());
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PaymentResponse response = paymentService.processPayment(1L, PaymentMethod.CREDIT_CARD);

        // Then
        assertEquals(PaymentStatus.COMPLETED, response.status());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order is not found")
    void processPayment_OrderNotFound_ThrowsException() {
        // Given
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> paymentService.processPayment(orderId, PaymentMethod.CREDIT_CARD));
    }

    @Test
    @DisplayName("Should throw PaymentProcessingException when payment method is not supported")
    void processPayment_UnsupportedMethod_ThrowsException() {
        // Given
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(100.0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentStrategies.stream()).thenReturn(Stream.empty());

        // When & Then
        assertThrows(PaymentProcessingException.class, () -> paymentService.processPayment(1L, PaymentMethod.BLIK));
    }
}