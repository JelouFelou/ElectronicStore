package com.example.electronicstore.repository;

import com.example.electronicstore.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Find user details by user ID
    Optional<UserDetails> findByUserId(Long userId);

    // Search by last name (case-insensitive)
    List<UserDetails> findByLastNameContainingIgnoreCase(String lastName);
}
