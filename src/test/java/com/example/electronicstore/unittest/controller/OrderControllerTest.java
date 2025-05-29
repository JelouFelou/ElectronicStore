package com.example.electronicstore.unittest.controller;

import com.example.electronicstore.config.TestSecurityConfig;
import com.example.electronicstore.controller.OrderController;
import com.example.electronicstore.dto.*;
import com.example.electronicstore.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import({TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private final ObjectMapper mapper = new ObjectMapper();

    /*@Test
    @DisplayName("POST /api/orders - Should create order (USER only, 200 OK)")
    @WithMockUser(roles = "USER")
    void createOrder_UserAccess_CreatesOrder() throws Exception {
        OrderRequest request = new OrderRequest(List.of(new OrderItemRequest(1L, 2)));

        OrderResponse response = new OrderResponse(
                1L,
                "NEW",
                LocalDateTime.now(),
                199.98,
                List.of(new OrderItemResponse(1L, "Product 1", 2, 99.99))
        );

        when(orderService.createOrder(any(OrderRequest.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.totalAmount").value(199.98))
                .andExpect(jsonPath("$.items[0].productId").value(1));
    }*/

    @Test
    @DisplayName("POST /api/orders - Should return 400 if items are empty")
    @WithMockUser(roles = "USER")
    void createOrder_EmptyItems_Returns400() throws Exception {
        // Given
        OrderRequest invalidRequest = new OrderRequest(List.of());

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}