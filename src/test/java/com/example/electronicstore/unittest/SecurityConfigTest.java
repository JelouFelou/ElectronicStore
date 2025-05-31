package com.example.electronicstore.unittest;

import com.example.electronicstore.config.SecurityConfig;
import com.example.electronicstore.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    @Test
    @DisplayName("PasswordEncoder should be BCrypt")
    void passwordEncoder_ShouldBeBCrypt() {
        SecurityConfig config = new SecurityConfig();
        assertTrue(config.passwordEncoder() instanceof BCryptPasswordEncoder);
    }


    @Test
    @DisplayName("Test PasswordEncoder should be NoOp")
    void passwordEncoder_ShouldBeNoOp() {
        TestSecurityConfig config = new TestSecurityConfig();
        assertSame(NoOpPasswordEncoder.getInstance(), config.passwordEncoder());
    }
}