package com.example.marketplace.controller;
import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SudoHomeController {

    private final ProductRepository productRepository;
    private final ImageService imageService;

    public SudoHomeController(ProductRepository productRepository,
                              ImageService imageService) {
        this.productRepository = productRepository;
        this.imageService = imageService;
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

    // Show the add-item form with the list of available images
    @GetMapping("/addItem")
    public String showAddItemForm(Model model) {
        model.addAttribute("images", imageService.getAvailableImages());
        return "addItem";
    }

    // Handle the form submission and save the new product
    @PostMapping("/addItem")
    public String addItem(@RequestParam String name,
                          @RequestParam double price,
                          @RequestParam String description,
                          @RequestParam String imageFilename) {
        String imageUrl = "/images/" + imageFilename;
        Product product = new Product(name, description, price, imageUrl);
        productRepository.save(product);
        return "redirect:/sudoHome";
    }
}