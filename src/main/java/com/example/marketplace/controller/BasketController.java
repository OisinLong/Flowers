package com.example.marketplace.controller;

import com.example.marketplace.model.Product;
import com.example.marketplace.service.BasketService;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BasketController {

    // Inject the basket service here
    @Autowired
    private BasketService basketService;

    // Inject the product repository to fetch products by id
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/basket")
    public String basket(Model model) {
        // Get all items in the basket
        model.addAttribute("items", basketService.getItems());

        // Calculate the total price
        model.addAttribute("total", basketService.getTotalPrice());

        // Return the basket view
        return "basket";
    }


    @GetMapping("/payment")
    public String payment() {
        return "payment"; // your payment.html page
    }

    @PostMapping("/basket/add/{id}")
    public String addToBasket(@PathVariable Long id) {
        // Get the product from the database
        Product product = productRepository.findById(id).orElseThrow();

        // Add it to the basket
        basketService.add(product);

        // Redirect back to the homepage
        return "redirect:/home";
    }
}
