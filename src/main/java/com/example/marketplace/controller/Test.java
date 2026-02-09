package com.example.marketplace.controller;

import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test{
    private final ProductRepository productRepository;
    public Test(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @GetMapping("/test")
    public String testDatabase() {
        // Create a Product
        Product product = new Product("testing", "This is a test product", 9.99);
        // Use the repository method save() to persist it in the database
        productRepository.save(product);
        return "test";
    }
}