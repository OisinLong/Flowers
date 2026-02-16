package com.example.marketplace.service;

import com.example.marketplace.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope; // Add this import
import java.io.Serializable; // Recommended for session storage
import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope // This creates a NEW service instance for every unique session/user
public class BasketService implements Serializable { // Serializable helps H2 save the object

    private final List<Product> items = new ArrayList<>();

    public void add(Product product) {
        items.add(product);
    }

    public List<Product> getItems() {
        return new ArrayList<>(items);
    }

    public void remove(Product product) {
        items.remove(product);
    }

    public void clear() {
        items.clear();
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(Product::getPrice).sum();
    }

    public int getItemCount() {
        return items.size();
    }
}