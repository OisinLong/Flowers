// Favourite entity — links a username to a Product (no quantity, live price)
package com.example.marketplace.model;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username", "product_id"}))
public class Favourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who favourited this product
    private String username;

    // Reference to the live Product row — price always up to date
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Favourite() {}

    public Favourite(String username, Product product) {
        this.username = username;
        this.product = product;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}

