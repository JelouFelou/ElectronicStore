package com.example.electronicstore.unittest.controller;

import com.example.electronicstore.config.TestSecurityConfig;
import com.example.electronicstore.controller.PaymentController;
import com.example.electronicstore.dto.*;
import com.example.electronicstore.entity.PaymentMethod;
import com.example.electronicstore.entity.PaymentStatus;
import com.example.electronicstore.exception.PaymentProcessingException;
import com.example.electronicstore.service.PaymentService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@Import({PaymentService.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("POST /api/payments - Should process payment (USER only, 200 OK)")
    @WithMockUser(roles = "USER")
    void processPayment_UserAccess_ProcessesPayment() throws Exception {
        // Given
        PaymentRequest request = new PaymentRequest(1L, PaymentMethod.CREDIT_CARD);
        PaymentResponse response = new PaymentResponse(1L, PaymentMethod.CREDIT_CARD, 199.98, PaymentStatus.COMPLETED, LocalDateTime.now());
        when(paymentService.processPayment(anyLong(), any(PaymentMethod.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .header("X-Username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("POST /api/payments - Should return 400 if order ID is invalid")
    @WithMockUser(roles = "USER")
    void processPayment_InvalidOrderId_Returns400() throws Exception {
        // Given
        PaymentRequest invalidRequest = new PaymentRequest(null, PaymentMethod.CREDIT_CARD);

        // When & Then
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest))
                        .header("X-Username", "testuser"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/payments - Should return 400 for unsupported payment method")
    @WithMockUser(roles = "USER")
    void processPayment_UnsupportedMethod_Returns400() throws Exception {
        PaymentRequest request = new PaymentRequest(1L, PaymentMethod.BLIK);

        when(paymentService.processPayment(anyLong(), any()))
                .thenThrow(new PaymentProcessingException("Unsupported method"));

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .header("X-Username", "testuser"))
                .andExpect(status().isBadRequest());
    }
}
