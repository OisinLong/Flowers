package com.example.marketplace.service;

import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class BasketService implements Serializable {
    // Map of Product -> Quantity
    private final Map<Long, Integer> items = new HashMap<>();

    @Autowired
    private transient ProductRepository productRepo; // transient so it's not serialized into H2

    public void add(Long productId) {
        items.put(productId, items.getOrDefault(productId, 0) + 1);
    }

    public void remove(Long productId) {
        items.remove(productId);
    }

    public Map<Product, Integer> getBasketContent() {
        Map<Product, Integer> content = new HashMap<>();
        items.forEach((id, qty) -> {
            content.put(productRepo.findById(id).orElseThrow(), qty);
        });
        return content;
    }

    public double getTotalPrice() {
        return getBasketContent().entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }
}