package com.example.electronicstore.unittest.controller;

import com.example.electronicstore.config.TestSecurityConfig;
import com.example.electronicstore.controller.UserController;
import com.example.electronicstore.dto.*;
import com.example.electronicstore.exception.ResourceNotFoundException;
import com.example.electronicstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
@Import({UserService.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/users/{id} - Should return user details (ADMIN only, 200 OK)")
    @WithMockUser(roles = "ADMIN")
    void getUserById_AdminAccess_ReturnsUser() throws Exception {
        // Given
        UserResponse response = new UserResponse(1L, "admin", "admin@example.com", "ADMIN");
        when(userService.getUserById(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@example.com"));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Should return 403 if user is not ADMIN")
    @WithMockUser(roles = "USER")
    void getUserById_NonAdminAccess_Returns403() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/users/{id} - Should return 404 if user not found")
    @WithMockUser(roles = "ADMIN")
    void getUserById_UserNotFound_Returns404() throws Exception {
        // Given
        when(userService.getUserById(999L)).thenThrow(ResourceNotFoundException.class);

        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}
