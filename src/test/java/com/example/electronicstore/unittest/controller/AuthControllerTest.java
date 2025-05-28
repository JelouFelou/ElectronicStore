package com.example.electronicstore.unittest.controller;

import com.example.electronicstore.config.TestSecurityConfig;
import com.example.electronicstore.controller.AuthController;
import com.example.electronicstore.dto.*;
import com.example.electronicstore.exception.EmailAlreadyExistsException;
import com.example.electronicstore.service.UserService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({UserService.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("POST /api/auth/register - Should register user (201 Created)")
    @WithMockUser
    void registerUser_ValidRequest_CreatesUser() throws Exception {
        // Given
        UserRequest request = new UserRequest("user1", "user1@example.com", "password123");
        UserResponse response = new UserResponse(1L, "user1", "user1@example.com", "USER");
        when(userService.registerUser(any(UserRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    @DisplayName("POST /api/auth/register - Should return 400 if email is invalid")
    @WithMockUser
    void registerUser_InvalidEmail_Returns400() throws Exception {
        // Given
        UserRequest invalidRequest = new UserRequest("user1", "invalid-email", "password123");

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/register - Should return 409 if email already exists")
    @WithMockUser
    void registerUser_DuplicateEmail_Returns409() throws Exception {
        // Given
        UserRequest request = new UserRequest("user1", "duplicate@example.com", "password123");

        when(userService.registerUser(any(UserRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already in use"));

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}