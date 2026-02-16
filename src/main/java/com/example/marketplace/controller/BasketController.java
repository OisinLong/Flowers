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

import java.util.ArrayList;
import java.util.List;

@Controller
public class BasketController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/basket")
    public String showBasket(HttpSession session, Model model) {
        // Retrieve the basket from the session (stored in H2)
        List<Product> basket = (List<Product>) session.getAttribute("basket");

        if (basket == null) {
            basket = new ArrayList<>();
        }

        // Calculate total price
        double total = basket.stream().mapToDouble(Product::getPrice).sum();

        model.addAttribute("items", basket);
        model.addAttribute("total", total);

        return "basket";
    }

    @PostMapping("/basket/add/{id}")
    public String addToBasket(@PathVariable Long id, HttpSession session) {
        // Find the product
        Product product = productRepository.findById(id).orElseThrow();

        // Get existing basket or create a new one
        List<Product> basket = (List<Product>) session.getAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();
        }

        // Add item and save back to session (triggers update in H2 SPRING_SESSION_ATTRIBUTES)
        basket.add(product);
        session.setAttribute("basket", basket);

        return "redirect:/home";
    }

    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }
}