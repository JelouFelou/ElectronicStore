package com.example.electronicstore.controller;

import com.example.electronicstore.dto.UserResponse;
import com.example.electronicstore.entity.User;
import com.example.electronicstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody UserService.UserRegisterDto dto) {
        User user = userService.registerUser(dto);
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}