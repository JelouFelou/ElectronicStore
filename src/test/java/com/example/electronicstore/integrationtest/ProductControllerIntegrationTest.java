package com.example.electronicstore.integrationtest;

import com.example.electronicstore.config.TestSecurityConfig;
import com.example.electronicstore.controller.ProductController;
import com.example.electronicstore.entity.Product;
import com.example.electronicstore.repository.ProductRepository;
import com.example.electronicstore.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({ProductService.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllProducts() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(1999.99);
        product.setStock(10);

        when(productRepository.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(1999.99))
                .andExpect(jsonPath("$[0].stock").value(10));
    }
}