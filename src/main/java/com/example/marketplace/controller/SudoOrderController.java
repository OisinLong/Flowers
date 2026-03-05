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

    // shows all orders still in processing
    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        // only admins should be poking around in here
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"admin".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("orders", orderService.findOrdersByStatus("Processing"));
        return "orders";
    }

    // takes the dropdown value and flips the status in h2
    @PostMapping("/orders/updateStatus/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, HttpSession session) {
        // only admins can update order statuses
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"admin".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        orderService.updateOrderStatus(id, status);
        return "redirect:/orders";
    }

    // shows only delivered orders + a revenue total
    @GetMapping("/orderHistory")
    public String orderHistory(HttpSession session, Model model) {
        // only admins can view all history
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"admin".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        var orders = orderService.findOrdersByStatus("Delivered");
        // sum of delivered orders; quick and easy dashboard number
        double totalRevenue = orders.stream()
                .map(o -> o.getTotalAmount() == null ? 0.0 : o.getTotalAmount())
                .reduce(0.0, Double::sum);
        model.addAttribute("orders", orders);
        model.addAttribute("totalRevenue", totalRevenue);
        return "orderHistory";
    }

    @GetMapping("/editOrder")
    public String editOrder(HttpSession session) {
        // only admins can access this
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"admin".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        return "editOrder";
    }
}
