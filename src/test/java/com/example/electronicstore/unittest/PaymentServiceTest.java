package com.example.electronicstore.unittest;

import com.example.electronicstore.dto.PaymentResponse;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setTotalAmount(100.0);
        order.setStatus(OrderStatus.PENDING);

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());

        // Symuluj zachowanie repozytoriów
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // When
        PaymentResponse result = paymentService.processPayment(orderId, PaymentMethod.CREDIT_CARD);

        // Then
        assertEquals(PaymentStatus.COMPLETED, result.status());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}