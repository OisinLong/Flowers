package com.example.marketplace.controller;
import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.service.ImageService;
import com.example.marketplace.service.AuthService;
import com.example.marketplace.service.OrderService;
import com.example.marketplace.model.User;
import jakarta.servlet.http.HttpSession;
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
    private final OrderService orderService;
    private final AuthService authService;

    public SudoHomeController(ProductRepository productRepository,
                              ImageService imageService,
                              OrderService orderService,
                              AuthService authService) {
        this.productRepository = productRepository;
        this.imageService = imageService;
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping("/sudoHome")
    public String sudoHome(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "asc") String sort,
            HttpSession session,
            Model model) {

        // Role guard: only admins can access this page
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";
        if (!"admin".equalsIgnoreCase(sessionUser.getRole())) return "redirect:/home";

        // Compute overall price bounds for the slider
        List<Product> all = productRepository.findAll();
        double overallMin = all.stream().mapToDouble(Product::getPrice).min().orElse(0);
        double overallMax = all.stream().mapToDouble(Product::getPrice).max().orElse(1000);

        // Default to full range if not supplied
        if (minPrice == null) minPrice = overallMin;
        if (maxPrice == null) maxPrice = overallMax;

        // Fetch filtered + sorted products
        List<Product> products;
        if ("desc".equals(sort)) {
            products = productRepository.findByPriceBetweenDesc(minPrice, maxPrice);
        } else {
            products = productRepository.findByPriceBetweenAsc(minPrice, maxPrice);
        }

        model.addAttribute("products", products);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("overallMin", overallMin);
        model.addAttribute("overallMax", overallMax);
        model.addAttribute("sort", sort);

        return "sudoHome";
    }


    // Show the edit-item form pre-filled with the existing product data
    @GetMapping("/editItem/{id}")
    public String editItem(@PathVariable Long id, HttpSession session, Model model) {
        // Role guard: only admins can edit items
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"admin".equalsIgnoreCase(user.getRole())) return "redirect:/home";

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
    public String showAddItemForm(HttpSession session, Model model) {
        // Role guard: only admins can add items
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"admin".equalsIgnoreCase(user.getRole())) return "redirect:/home";

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

    @GetMapping("/sudoProfile")
    public String sudoProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        return "sudoProfile";
    }

    // Show the admin change password page
    @GetMapping("/sudoChangePassword")
    public String showChangePassword(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "sudoChangePassword";
    }

    // Handle password change from the admin change password page
    @PostMapping("/sudoChangePassword")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Check that new password and confirmation match
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/sudoChangePassword?pwError=mismatch";
        }

        // Attempt password change via AuthService
        boolean changed = authService.changePassword(user.getUsername(), currentPassword, newPassword);
        if (changed) {
            return "redirect:/sudoChangePassword?pwSuccess=true";
        }
        return "redirect:/sudoChangePassword?pwError=wrong";
    }
}