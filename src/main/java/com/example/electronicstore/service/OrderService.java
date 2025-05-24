package com.example.electronicstore.service;

import com.example.electronicstore.dto.OrderItemRequest;
import com.example.electronicstore.dto.OrderItemResponse;
import com.example.electronicstore.dto.OrderRequest;
import com.example.electronicstore.dto.OrderResponse;
import com.example.electronicstore.entity.*;
import com.example.electronicstore.exception.InsufficientStockException;
import com.example.electronicstore.exception.ResourceNotFoundException;
import com.example.electronicstore.repository.OrderRepository;
import com.example.electronicstore.repository.ProductRepository;
import com.example.electronicstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderResponse createOrder(OrderRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (product.getStock() < itemRequest.quantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(product.getPrice());
            item.setOrder(order);

            items.add(item);
            total += product.getPrice() * itemRequest.quantity();
            product.setStock(product.getStock() - itemRequest.quantity());
        }

        order.setOrderItems(items);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.NEW);

        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getTotalAmount(),
                order.getOrderItems().stream()
                        .map(this::convertToItemResponse)
                        .toList()
        );
    }

    private OrderItemResponse convertToItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }
}