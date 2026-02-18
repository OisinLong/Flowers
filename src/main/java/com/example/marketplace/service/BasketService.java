package com.example.marketplace.service;

import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@SessionScope
public class BasketService implements Serializable {
    private static final long serialVersionUID = 1L;

    // The raw data: Product ID -> Quantity
    private final Map<Long, Integer> items = new LinkedHashMap<>();

    @Autowired
    private transient ProductRepository productRepository;

    public void addItem(Long id) {
        items.put(id, items.getOrDefault(id, 0) + 1);
    }

    public void removeItem(Long id) {
        items.remove(id);
    }

    // Adds one more to the quantity
    public void incrementItem(Long id) {
        items.put(id, items.getOrDefault(id, 0) + 1);
    }

    // Subtracts one, or removes the item entirely if quantity hits zero
    public void decrementItem(Long id) {
        if (items.containsKey(id)) {
            int currentQty = items.get(id);
            if (currentQty > 1) {
                items.put(id, currentQty - 1);
            } else {
                items.remove(id);
            }
        }
    }

    // Business Logic: Transforms IDs into Objects for the View
    public Map<Product, Integer> getBasketContent() {
        Map<Product, Integer> content = new HashMap<>();
        items.forEach((id, qty) ->
                productRepository.findById(id).ifPresent(p -> content.put(p, qty))
        );
        return content;
    }

    public double getTotalPrice() {
        return getBasketContent().entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }
}