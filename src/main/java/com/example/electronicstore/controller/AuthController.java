package com.example.electronicstore.controller;

import com.example.electronicstore.dto.UserRequest;
import com.example.electronicstore.dto.UserResponse;
import com.example.electronicstore.exception.EmailAlreadyExistsException;
import com.example.electronicstore.exception.ErrorResponse;
import com.example.electronicstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "User registration and authentication endpoints")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Register new user",
            description = "Creates a new user account with the USER role. The email must be unique."
    )
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest request) {
        try {
            UserResponse response = userService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(
                            HttpStatus.CONFLICT.value(),
                            e.getMessage(),
                            System.currentTimeMillis()
                    ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestParam String username,
            @RequestParam String password
    ) {
        return ResponseEntity.ok().build();
    }
}