package com.example.marketplace.controller;
import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {
    private final ProductRepository productRepository;
    public HomeController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products); // add all products
        return "home";
    }

    @GetMapping("/ordersUser")
    public String orders() {
        return "ordersUser";
    }

    @GetMapping("/item/{id}")
    public String item(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "item";
    }

}