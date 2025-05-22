package com.example.electronicstore.service;

import com.example.electronicstore.entity.Product;
import com.example.electronicstore.repository.OrderItemRepository;
import com.example.electronicstore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public Product createProduct(ProductCreateDto dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        product.setCategory(dto.category());
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductUpdateDto dto) {
        Product product = getProductById(id);
        if (dto.name() != null) product.setName(dto.name());
        if (dto.price() > 0) product.setPrice(dto.price());
        if (dto.stock() >= 0) product.setStock(dto.stock());
        if (dto.category() != null) product.setCategory(dto.category());
        return productRepository.save(product);
    }

    // Supporting DTOs as inner classes
    public record ProductCreateDto(
            @NotBlank String name,
            @Positive Double price,
            @PositiveOrZero Integer stock,
            String category
    ) {}

    public record ProductUpdateDto(
            String name,
            @Positive Double price,
            @PositiveOrZero Integer stock,
            String category
    ) {}
}
