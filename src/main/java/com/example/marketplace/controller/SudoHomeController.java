package com.example.marketplace.controller;
import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // Show the edit-item form pre-filled with the existing product data
    @GetMapping("/editItem/{id}")
    public String editItem(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        model.addAttribute("images", imageService.getAvailableImages());
        return "editItem";
    }

    // Handle the edit-item form submission and update the product
    @PostMapping("/editItem/{id}")
    public String updateItem(@PathVariable Long id,
                             @RequestParam String name,
                             @RequestParam double price,
                             @RequestParam String description,
                             @RequestParam String imageFilename) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setImageUrl("/images/" + imageFilename);
        productRepository.save(product);
        return "redirect:/sudoHome";
    }

    // Delete the product entirely and redirect back to sudoHome
    @PostMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/sudoHome";
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