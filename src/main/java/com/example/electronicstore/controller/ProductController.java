package com.example.electronicstore.controller;

import com.example.electronicstore.dto.ProductRequest;
import com.example.electronicstore.dto.ProductResponse;
import com.example.electronicstore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Products", description = "Endpoints for managing electronic products")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get paginated products", description = "Returns paginated list of all products")
    @GetMapping
    public Page<ProductResponse> getAllProducts(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(size = 20, sort = "name") Pageable pageable
    ) {
        log.info("Fetching all products - page {}", pageable.getPageNumber());
        return productService.getAllProducts(pageable);
    }

    @Operation(summary = "Get product by ID", description = "Returns product details by its ID")
    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long id
    ) {
        log.info("Fetching product with ID: {}", id);
        return productService.getProductById(id);
    }

    @Operation(summary = "Create new product", description = "Creates new product (Admin only)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse createProduct(
            @Parameter(description = "Product creation data", required = true)
            @Valid @RequestBody ProductRequest request
    ) {
        log.info("Admin creating new product: {}", request.name());
        return productService.createProduct(request);
    }

    @Operation(summary = "Update product", description = "Updates existing product (Admin only)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Product update data", required = true)
            @Valid @RequestBody ProductRequest request
    ) {
        log.info("Admin updating product ID {}: {}", id, request);
        return productService.updateProduct(id, request);
    }

    @Operation(summary = "Update product stock", description = "Updates product stock quantity (Admin only)")
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateStock(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long id,
            @Parameter(description = "Stock change value", required = true)
            @RequestParam int quantityChange
    ) {
        log.info("Admin updating stock for product ID {}: change {}", id, quantityChange);
        return productService.updateProductStock(id, quantityChange);
    }

    @Operation(summary = "Delete product", description = "Deletes product by ID (Admin only)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable Long id
    ) {
        log.warn("Admin deleting product with ID: {}", id);
        productService.deleteProduct(id);
    }
}