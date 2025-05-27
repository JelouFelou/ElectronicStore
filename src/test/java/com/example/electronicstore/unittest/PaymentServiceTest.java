package com.example.electronicstore.unittest;

import com.example.electronicstore.dto.PaymentResponse;
import com.example.electronicstore.entity.*;
import com.example.electronicstore.repository.OrderRepository;
import com.example.electronicstore.repository.PaymentRepository;
import com.example.electronicstore.service.PaymentService;
import com.example.electronicstore.strategy.CreditCardPaymentStrategy;
import com.example.electronicstore.strategy.PaymentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
}