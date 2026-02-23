package com.example.marketplace.controller;
import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SudoHomeController {

    private final ProductRepository productRepository;

    public SudoHomeController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/sudoHome")
    public String sudoHome(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "sudoHome";
    }

    @GetMapping("/orderHistory")
    public String orderHistory() {
        return "orderHistory";
    }

    @GetMapping("/editItem")
    public String editItem() {
        return "editItem";
    }
}