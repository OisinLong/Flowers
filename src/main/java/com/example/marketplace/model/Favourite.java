// username -> product (no qty), price stays live since we reference Product
package com.example.marketplace.model;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username", "product_id"}))
public class Favourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // who favourited it
    private String username;

    // points to the live Product row
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Favourite() {}

    public Favourite(String username, Product product) {
        this.username = username;
        this.product = product;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
