package com.example.electronicstore.unittest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class PasswordEncoderTest {

    @Test
    @DisplayName("BCrypt should encode and match password")
    void shouldEncodeAndMatchPassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "secret123";
        String encodedPassword = encoder.encode(rawPassword);

        assertTrue(encoder.matches(rawPassword, encodedPassword));
        assertFalse(encoder.matches("wrongPassword", encodedPassword));
    }

    @Test
    @DisplayName("NoOp should not encode passwords")
    void noOpEncoder_ShouldNotEncode() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        String rawPassword = "secret123";

        assertEquals(rawPassword, encoder.encode(rawPassword));
        assertTrue(encoder.matches(rawPassword, rawPassword));
    }

    @Test
    @DisplayName("BCrypt should generate different hashes for same password")
    void shouldGenerateDifferentHashes() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "secret123";
        String encoded1 = encoder.encode(rawPassword);
        String encoded2 = encoder.encode(rawPassword);

        assertNotEquals(encoded1, encoded2);
        assertTrue(encoder.matches(rawPassword, encoded1));
        assertTrue(encoder.matches(rawPassword, encoded2));
    }
}