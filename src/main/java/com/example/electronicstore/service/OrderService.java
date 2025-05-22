package com.example.electronicstore.service;

import com.example.electronicstore.entity.*;
import com.example.electronicstore.repository.OrderRepository;
import com.example.electronicstore.repository.ProductRepository;
import com.example.electronicstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    public Order createOrder(OrderCreateDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (OrderItemDto itemDto : dto.items()) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < itemDto.quantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDto.quantity());
            item.setUnitPrice(product.getPrice());
            item.setOrder(order);

            items.add(item);
            total += product.getPrice() * itemDto.quantity();
            product.setStock(product.getStock() - itemDto.quantity());
        }

        order.setOrderItems(items);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.NEW);
        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Supporting DTOs
    public record OrderCreateDto(
            List<OrderItemDto> items
    ) {}

    public record OrderItemDto(
            @NotNull Long productId,
            @Positive Integer quantity
    ) {}
}