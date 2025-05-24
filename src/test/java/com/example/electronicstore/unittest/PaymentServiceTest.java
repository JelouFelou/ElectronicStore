package com.example.electronicstore.unittest;

import com.example.electronicstore.entity.*;
import com.example.electronicstore.repository.OrderRepository;
import com.example.electronicstore.repository.PaymentRepository;
import com.example.electronicstore.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("Should process payment successfully")
    void processPayment_Success() {
        // Given
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(100.0);
        order.setStatus(OrderStatus.PENDING); // Dodane dla kompletności

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.COMPLETED);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        Payment result = paymentService.processPayment(order, PaymentMethod.CREDIT_CARD);

        // Then
        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
        verify(orderRepository, times(1)).save(order);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}