package com.example.electronicstore.service;

import com.example.electronicstore.entity.User;
import com.example.electronicstore.entity.UserRole;
import com.example.electronicstore.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserRegisterDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(UserRole.USER);

        return userRepository.save(user);
    }

    public record UserRegisterDto(
            @NotBlank String username,
            @Email String email,
            @Size(min = 8) String password
    ) {}
}