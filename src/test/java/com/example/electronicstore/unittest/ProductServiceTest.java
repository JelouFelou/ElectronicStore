package com.example.electronicstore.unittest;

import com.example.electronicstore.dto.ProductRequest;
import com.example.electronicstore.dto.ProductResponse;
import com.example.electronicstore.entity.Product;
import com.example.electronicstore.exception.ResourceNotFoundException;
import com.example.electronicstore.repository.ProductRepository;
import com.example.electronicstore.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Should update product stock successfully")
    void updateProductStock_Success() {
        // Given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setStock(10);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ProductResponse response = productService.updateProductStock(productId, 5);

        // Then
        assertEquals(15, response.stock());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Should throw exception when stock is insufficient")
    void updateProductStock_InsufficientStock_ThrowsException() {
        // Given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setStock(5);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When & Then
        assertThrows(RuntimeException.class, () -> productService.updateProductStock(productId, -10));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent product")
    void updateProduct_ProductNotFound_ThrowsException() {
        // Given
        Long productId = 999L;
        ProductRequest request = new ProductRequest("Laptop", 1999.99, 10, "Electronics");
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productId, request));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent product")
    void deleteProduct_ProductNotFound_ThrowsException() {
        // Given
        Long productId = 999L;

        // When
        productService.deleteProduct(productId);

        // Then
        verify(productRepository, times(1)).deleteById(productId);
    }
}
