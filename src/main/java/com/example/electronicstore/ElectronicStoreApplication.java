package com.example.electronicstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.electronicstore.entity")
public class ElectronicStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectronicStoreApplication.class, args);
    }
}