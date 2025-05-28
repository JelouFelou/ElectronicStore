package com.example.electronicstore.unittest.controller;

import com.example.electronicstore.config.TestSecurityConfig;
import com.example.electronicstore.controller.ProductController;
import com.example.electronicstore.dto.ProductRequest;
import com.example.electronicstore.dto.ProductResponse;
import com.example.electronicstore.exception.ResourceNotFoundException;
import com.example.electronicstore.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ProductController.class)
@Import({ProductService.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/products - Should return paginated products (200 OK)")
    @WithMockUser
    void getAllProducts_ReturnsPaginatedList() throws Exception {
        // Given
        ProductResponse product = new ProductResponse(1L, "Laptop", 1999.99, 10, "Electronics");
        Page<ProductResponse> page = new PageImpl<>(List.of(product));
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is("Laptop")));
    }

    @Test
    @DisplayName("GET /api/products/{id} - Should return product by ID (200 OK)")
    @WithMockUser
    void getProductById_ReturnsProduct() throws Exception {
        // Given
        ProductResponse product = new ProductResponse(1L, "Laptop", 1999.99, 10, "Electronics");
        when(productService.getProductById(1L)).thenReturn(product);

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    @DisplayName("GET /api/products/{id} - Should return 404 if product not found")
    @WithMockUser
    void getProductById_NotFound_Returns404() throws Exception {
        // Given
        when(productService.getProductById(999L))
                .thenThrow(new ResourceNotFoundException("Product not found with id: 999"));

        // When & Then
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found with id: 999"));
    }

    @Test
    @DisplayName("POST /api/products - Should create product (ADMIN only, 201 Created)")
    @WithMockUser(roles = "ADMIN")
    void createProduct_AdminAccess_CreatesProduct() throws Exception {
        // Given
        ProductRequest request = new ProductRequest("Laptop", 1999.99, 10, "Electronics");
        ProductResponse response = new ProductResponse(1L, "Laptop", 1999.99, 10, "Electronics");
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("POST /api/products - Should return 403 if user is not ADMIN")
    @WithMockUser(roles = "USER")
    void createProduct_NonAdminAccess_Returns403() throws Exception {
        // Given
        ProductRequest request = new ProductRequest("Laptop", 1999.99, 10, "Electronics");

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}