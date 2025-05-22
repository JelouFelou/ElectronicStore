package com.example.electronicstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String address;
    private String city;
    private String firstName;
    private String lastName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
