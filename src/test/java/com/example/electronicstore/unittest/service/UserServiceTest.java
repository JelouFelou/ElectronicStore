package com.example.electronicstore.unittest.service;

import com.example.electronicstore.dto.UserRequest;
import com.example.electronicstore.dto.UserResponse;
import com.example.electronicstore.entity.User;
import com.example.electronicstore.entity.UserRole;
import com.example.electronicstore.exception.EmailAlreadyExistsException;
import com.example.electronicstore.exception.ResourceNotFoundException;
import com.example.electronicstore.repository.UserRepository;
import com.example.electronicstore.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should register new user successfully")
    void registerUser_Success() {
        // Given
        UserRequest request = new UserRequest("user1", "user1@example.com", "password123");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(request.username());
        savedUser.setEmail(request.email());
        savedUser.setRole(UserRole.USER);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserResponse response = userService.registerUser(request);

        // Then
        assertEquals(request.username(), response.username());
        assertEquals(request.email(), response.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void registerUser_EmailExists_ThrowsException() {
        // Given
        UserRequest request = new UserRequest("user1", "user1@example.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // When & Then
        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email is duplicated")
    void registerUser_DuplicateEmail_ThrowsException() {
        // Given
        UserRequest request = new UserRequest("user1", "user1@example.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // When & Then
        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user is not found")
    void getUserById_UserNotFound_ThrowsException() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }
}
