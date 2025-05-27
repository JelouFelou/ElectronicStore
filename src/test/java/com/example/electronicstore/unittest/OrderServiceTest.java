package com.example.electronicstore.unittest;

import com.example.electronicstore.dto.OrderItemRequest;
import com.example.electronicstore.dto.OrderRequest;
import com.example.electronicstore.dto.OrderResponse;
import com.example.electronicstore.entity.Order;
import com.example.electronicstore.entity.Product;
import com.example.electronicstore.entity.User;
import com.example.electronicstore.exception.InsufficientStockException;
import com.example.electronicstore.exception.ResourceNotFoundException;
import com.example.electronicstore.repository.OrderRepository;
import com.example.electronicstore.repository.ProductRepository;
import com.example.electronicstore.repository.UserRepository;
import com.example.electronicstore.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Should create order successfully")
    void createOrder_Success() {
        // Given
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(100.0);
        product.setStock(10);

        OrderRequest request = new OrderRequest(List.of(new OrderItemRequest(1L, 2)));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderResponse response = orderService.createOrder(request, "testUser");

        // Then
        assertEquals(200.0, response.totalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw InsufficientStockException when product stock is too low")
    void createOrder_InsufficientStock_ThrowsException() {
        // Given
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setStock(1); // Tylko 1 sztuka w magazynie

        OrderRequest request = new OrderRequest(List.of(new OrderItemRequest(1L, 2))); // Żądanie 2 sztuk

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When & Then
        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(request, "testUser"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order is not found")
    void getOrderById_OrderNotFound_ThrowsException() {
        // Given
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));
    }
}
