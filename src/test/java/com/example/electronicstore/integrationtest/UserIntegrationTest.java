package com.example.electronicstore.integrationtest;

import com.example.electronicstore.dto.UserRequest;
import com.example.electronicstore.dto.UserResponse;
import com.example.electronicstore.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void registerUserAndGetDetails() {
        // Użyj unikalnego adresu email dla każdego testu
        String uniqueEmail = "test-" + System.currentTimeMillis() + "@example.com";
        String username = "user-" + System.currentTimeMillis();

        // Rejestracja nowego użytkownika
        UserRequest request = new UserRequest(username, uniqueEmail, "Password123!");
        ResponseEntity<UserResponse> registerResponse = restTemplate.postForEntity(
                "/api/auth/register",
                request,
                UserResponse.class
        );

        // Wypisz odpowiedź w przypadku błędu
        if (registerResponse.getStatusCode() != HttpStatus.OK) {
            System.err.println("Register response: " + registerResponse);
        }

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserResponse userResponse = registerResponse.getBody();
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.email()).isEqualTo(uniqueEmail);

        // Pobieranie danych użytkownika jako admin
        ResponseEntity<UserResponse> userDetailsResponse = restTemplate
                .withBasicAuth("admin", "admin") // Użyj poprawnych danych z application-test.properties
                .getForEntity("/api/users/" + userResponse.id(), UserResponse.class);

        // Wypisz odpowiedź w przypadku błędu
        if (userDetailsResponse.getStatusCode() != HttpStatus.OK) {
            System.err.println("User details response: " + userDetailsResponse);
        }

        assertThat(userDetailsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserResponse details = userDetailsResponse.getBody();
        assertThat(details).isNotNull();
        assertThat(details.username()).isEqualTo(username);
        assertThat(details.role()).isEqualTo(UserRole.USER.name());
    }
}