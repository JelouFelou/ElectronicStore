package com.example.electronicstore.repository;

import com.example.electronicstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by username (for login)
    Optional<User> findByUsername(String username);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if username exists (for registration)
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Get users by role (e.g., for admin panel)
    List<User> findByRole(Role role);
}

