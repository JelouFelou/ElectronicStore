package com.example.electronicstore.controller;

import com.example.electronicstore.entity.User;
import com.example.electronicstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserService.UserRegisterDto dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }
}