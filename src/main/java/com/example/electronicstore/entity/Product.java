package com.example.electronicstore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stock;

    private String category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
}
