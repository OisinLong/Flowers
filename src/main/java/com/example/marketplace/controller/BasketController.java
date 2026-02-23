package com.example.marketplace.controller;

import com.example.marketplace.model.User;
import com.example.marketplace.service.BasketService;
import com.example.marketplace.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BasketController {

    @Autowired
    private BasketService basketService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/basket")
    public String viewBasket(Model model) {
        // The Controller just asks the Service for data and passes it to the View
        model.addAttribute("items", basketService.getBasketContent());
        model.addAttribute("total", basketService.getTotalPrice());
        return "basket";
    }

    @PostMapping("/basket/add/{id}")
    public String addToBasket(@PathVariable Long id) {
        basketService.addItem(id);
        return "redirect:/basket";
    }

    @PostMapping("/basket/remove/{id}")
    public String removeFromBasket(@PathVariable Long id) {
        basketService.removeItem(id);
        return "redirect:/basket";
    }

    @PostMapping("/basket/increment/{id}")
    public String increment(@PathVariable Long id) {
        basketService.incrementItem(id);
        return "redirect:/basket";
    }

    @PostMapping("/basket/decrement/{id}")
    public String decrement(@PathVariable Long id) {
        basketService.decrementItem(id);
        return "redirect:/basket";
    }

    // Shows the payment confirmation page
    @GetMapping("/payment")
    public String showPayment(Model model) {
        model.addAttribute("total", basketService.getTotalPrice());
        return "payment";
    }

    @PostMapping("/payment/confirm")
    public String confirmPayment(HttpSession session) {
        // 1. Retrieve the User object from the session
        User user = (User) session.getAttribute("user");

        // 2. Safety Check: If the session expired or user isn't logged in
        if (user == null) {
            return "redirect:/login";
        }

        String username = user.getUsername(); // Assuming your User model has getUsername()

        // 3. Get basket data
        var content = basketService.getBasketContent();
        double total = basketService.getTotalPrice();

        if (content.isEmpty()) {
            return "redirect:/basket";
        }

        // 4. Save to H2 Database
        orderService.saveOrder(username, content, total);

        // 5. Wipe the session basket
        basketService.clear();

        return "redirect:/home";
    }

}