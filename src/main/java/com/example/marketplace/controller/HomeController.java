package com.example.marketplace.controller;
import com.example.marketplace.model.Order;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.OrderRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public HomeController(ProductRepository productRepository,
                          OrderRepository orderRepository,
                          OrderService orderService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products); // add all products
        return "home";
    }

    @GetMapping("/ordersUser")
    public String viewUserOrders(HttpSession session, Model model) {
        // 1. Get the logged-in user from the session
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login"; // Safety first!
        }

        // 2. Fetch only THIS user's orders
        List<Order> userOrders = orderService.findOrdersByUsername(user.getUsername());

        // 3. Push to the HTML
        model.addAttribute("orders", userOrders);

        return "ordersUser";
    }

    @GetMapping("/item/{id}")
    public String item(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "item";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        String username = user.getUsername();

        double totalSpend = orderRepository.findByUsername(username).stream()
                .map(o -> o.getTotalAmount() == null ? 0.0 : o.getTotalAmount())
                .reduce(0.0, Double::sum);

        // Simple status derived from spend (adjust if you have a real field)
        String status = totalSpend > 0 ? "Active" : "New";

        model.addAttribute("username", username);
        model.addAttribute("status", status);
        model.addAttribute("totalSpend", totalSpend);

        return "profile";
    }

}