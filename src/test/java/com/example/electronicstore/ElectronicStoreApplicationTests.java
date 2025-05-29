package com.example.electronicstore;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.example.electronicstore.config.TestSecurityConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ElectronicStoreApplicationTests {

    @Test
    @DisplayName("Context should load")
    void contextLoads() {
    }
}