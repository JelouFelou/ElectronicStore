package com.example.electronicstore.controller;

import com.example.electronicstore.dto.UserRequest;
import com.example.electronicstore.dto.UserResponse;
import com.example.electronicstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "User registration and authentication endpoints")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Register new user", description = "Creates a new user account with USER role")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody UserRequest request
    ) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }
}