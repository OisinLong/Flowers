package com.example.marketplace.controller;

import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

// Controller for managing basket - stored in session and persisted to H2 via spring-session-jdbc
@Controller
public class BasketController {

    // Repository for fetching product details from the database
    @Autowired
    private ProductRepository productRepository;

    // Displays the basket page with all items and total price
    @GetMapping("/basket")
    public String basket(Model model, HttpSession session) {
        // Retrieve the basket from session (Map of productId -> quantity)
        @SuppressWarnings("unchecked")
        Map<Long, Integer> basket = (Map<Long, Integer>) session.getAttribute("basket");

        // If no basket exists yet, create an empty one
        if (basket == null) basket = new HashMap<>();

        // Convert product IDs to actual Product objects for display
        Map<Product, Integer> content = new HashMap<>();
        basket.forEach((id, qty) ->
                productRepository.findById(id).ifPresent(p -> content.put(p, qty))
        );

        // Calculate the total price (price * quantity for each item)
        double total = content.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();

        // Add data to model for Thymeleaf template
        model.addAttribute("items", content);
        model.addAttribute("total", total);
        return "basket";
    }

    // Adds a product to the basket or increments quantity if already present
    @PostMapping("/basket/add/{id}")
    public String addToBasket(@PathVariable Long id, HttpSession session) {
        // Retrieve existing basket from session
        @SuppressWarnings("unchecked")
        Map<Long, Integer> basket = (Map<Long, Integer>) session.getAttribute("basket");

        // Create new basket if none exists
        if (basket == null) basket = new HashMap<>();

        // Add product or increment quantity
        basket.put(id, basket.getOrDefault(id, 0) + 1);

        // Save the updated basket back to session (persisted to H2 via spring-session-jdbc)
        session.setAttribute("basket", basket);
        return "redirect:/basket";
    }

    // Removes a product entirely from the basket
    @PostMapping("/basket/remove/{id}")
    public String removeFromBasket(@PathVariable Long id, HttpSession session) {
        // Retrieve existing basket from session
        @SuppressWarnings("unchecked")
        Map<Long, Integer> basket = (Map<Long, Integer>) session.getAttribute("basket");

        // Only remove if basket exists
        if (basket != null) {
            basket.remove(id);
            // Save updated basket back to session
            session.setAttribute("basket", basket);
        }
        return "redirect:/basket";
    }

    // Displays the payment page
    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }
}
