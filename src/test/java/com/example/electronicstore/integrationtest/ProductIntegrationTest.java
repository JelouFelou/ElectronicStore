package com.example.electronicstore.integrationtest;

import com.example.electronicstore.dto.ProductRequest;
import com.example.electronicstore.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpEntity<?> createAdminEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<?> createAdminEntity() {
        return createAdminEntity(null);
    }

    @Test
    void fullProductLifecycle() {
        ProductRequest createRequest = new ProductRequest("Laptop", 1999.99, 10, "Electronics");
        ResponseEntity<ProductResponse> createResponse = restTemplate.exchange(
                "/api/products",
                HttpMethod.POST,
                createAdminEntity(createRequest),
                ProductResponse.class
        );

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ProductResponse createdProduct = createResponse.getBody();
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.name()).isEqualTo("Laptop");

        ProductRequest updateRequest = new ProductRequest("Updated Laptop", 2999.99, 20, "Electronics");
        ResponseEntity<ProductResponse> updateResponse = restTemplate.exchange(
                "/api/products/" + createdProduct.id(),
                HttpMethod.PUT,
                createAdminEntity(updateRequest),
                ProductResponse.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponse updatedProduct = updateResponse.getBody();
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.price()).isEqualTo(2999.99);

        ResponseEntity<ProductResponse> getResponse = restTemplate.getForEntity(
                "/api/products/" + createdProduct.id(),
                ProductResponse.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/products/" + createdProduct.id(),
                HttpMethod.DELETE,
                createAdminEntity(),
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}