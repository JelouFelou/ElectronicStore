package com.example.electronicstore.unittest.dto;

import com.example.electronicstore.dto.OrderItemRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


import static org.junit.jupiter.api.Assertions.*;

class OrderItemRequestTest {

    @Test
    @DisplayName("OrderItemRequest should correctly initialize")
    void shouldInitializeCorrectly() {
        OrderItemRequest request = new OrderItemRequest(1L, 5);

        assertEquals(1L, request.productId());
        assertEquals(5, request.quantity());
    }

    @Test
    @DisplayName("OrderItemRequest equals and hashCode")
    void testEquality() {
        OrderItemRequest request1 = new OrderItemRequest(1L, 5);
        OrderItemRequest request2 = new OrderItemRequest(1L, 5);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}
