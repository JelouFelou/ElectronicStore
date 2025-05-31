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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Products", description = "Endpoints for managing electronic products")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Get all products",
            description = "Retrieves a complete list of all products in the catalog. " +
                    "Suitable for small catalogs where pagination is not needed."
    )
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        return productService.getAllProducts();
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieves the details of a product by its unique ID."
    )
    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long id
    ) {
        log.info("Fetching product with ID: {}", id);
        return productService.getProductById(id);
    }

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product in the catalog. " +
                    "This operation is typically restricted to administrators."
    )
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

    @Operation(
            summary = "Update a product",
            description = "Updates an existing product by its ID. " +
                    "This operation is typically restricted to administrators."
    )
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

    @Operation(
            summary = "Update product stock",
            description = "Adjusts the stock quantity of a product. " +
                    "Positive numbers increase stock, negative decrease. " +
                    "This operation is typically restricted to administrators."
    )
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

    @Operation(
            summary = "Delete a product",
            description = "Removes a product from the catalog by its ID. " +
                    "This operation is typically restricted to administrators."
    )
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