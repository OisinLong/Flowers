package com.example.marketplace.controller;

import com.example.marketplace.model.User;
import com.example.marketplace.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SudoOrderController {

    @Autowired
    private OrderService orderService;

    // Shows all orders currently in "Processing" status
    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        // Role guard: only admins can view all orders
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"admin".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("orders", orderService.findOrdersByStatus("Processing"));
        return "orders";
    }

    // Receives the dropdown form submission and updates the order status in the DB
    @PostMapping("/orders/updateStatus/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/orders";
    }

    // Shows only "Delivered" orders in order history with total revenue
    @GetMapping("/orderHistory")
    public String orderHistory(HttpSession session, Model model) {
        // Role guard: only admins can view order history
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"admin".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        var orders = orderService.findOrdersByStatus("Delivered");
        // Sum up the total amount of every delivered order
        double totalRevenue = orders.stream()
                .map(o -> o.getTotalAmount() == null ? 0.0 : o.getTotalAmount())
                .reduce(0.0, Double::sum);
        model.addAttribute("orders", orders);
        model.addAttribute("totalRevenue", totalRevenue);
        return "orderHistory";
    }

    @GetMapping("/editOrder")
    public String editOrder() {
        return "editOrder";
    }
}

