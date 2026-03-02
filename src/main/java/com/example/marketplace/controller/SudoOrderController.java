package com.example.marketplace.controller;

import com.example.marketplace.service.OrderService;
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
    public String orders(Model model) {
        model.addAttribute("orders", orderService.findOrdersByStatus("Processing"));
        return "orders";
    }

    // Receives the dropdown form submission and updates the order status in the DB
    @PostMapping("/orders/updateStatus/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/orders";
    }

    // Shows only "Delivered" orders in order history
    @GetMapping("/orderHistory")
    public String orderHistory(Model model) {
        model.addAttribute("orders", orderService.findOrdersByStatus("Delivered"));
        return "orderHistory";
    }

    @GetMapping("/editOrder")
    public String editOrder() {
        return "editOrder";
    }
}

