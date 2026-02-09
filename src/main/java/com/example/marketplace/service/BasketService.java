package com.example.marketplace.service;

import com.example.marketplace.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasketService {

    // Simple list to hold products added to the basket
    private final List<Product> items = new ArrayList<>();

    /**
     * Add a product to the basket
     * @param product the product to add
     */
    public void add(Product product) {
        items.add(product);
    }

    /**
     * Get all products currently in the basket
     * @return a list of products
     */
    public List<Product> getItems() {
        return new ArrayList<>(items); // return a copy to prevent external modification
    }

    /**
     * Remove a product from the basket
     * @param product the product to remove
     */
    public void remove(Product product) {
        items.remove(product);
    }

    /**
     * Clear the basket
     */
    public void clear() {
        items.clear();
    }

    /**
     * Get the total price of all items in the basket
     * @return total price
     */
    public double getTotalPrice() {
        return items.stream().mapToDouble(Product::getPrice).sum();
    }

    /**
     * Get the number of items in the basket
     * @return item count
     */
    public int getItemCount() {
        return items.size();
    }
}
