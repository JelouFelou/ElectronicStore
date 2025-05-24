package com.example.electronicstore.unittest;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class PasswordEncoderTest {

    @Test
    void shouldEncodeAndMatchPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "secret123";
        String encodedPassword = encoder.encode(rawPassword);

        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }
}