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
    public String viewBasket(HttpSession session, Model model) {
        // only logged-in customers can use the basket
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/sudoHome";

        // controller stays dumb; service does the work
        model.addAttribute("items", basketService.getBasketContent());
        model.addAttribute("total", basketService.getTotalPrice());
        return "basket";
    }

    @PostMapping("/basket/add/{id}")
    public String addToBasket(@PathVariable Long id, HttpSession session) {
        // only logged-in customers can use the basket
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/sudoHome";

        basketService.addItem(id);
        return "redirect:/basket";
    }

    @PostMapping("/basket/remove/{id}")
    public String removeFromBasket(@PathVariable Long id, HttpSession session) {
        // only logged-in customers can use the basket
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/sudoHome";

        basketService.removeItem(id);
        return "redirect:/basket";
    }

    @PostMapping("/basket/increment/{id}")
    public String increment(@PathVariable Long id, HttpSession session) {
        // only logged-in customers can use the basket
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/sudoHome";

        basketService.incrementItem(id);
        return "redirect:/basket";
    }

    @PostMapping("/basket/decrement/{id}")
    public String decrement(@PathVariable Long id, HttpSession session) {
        // only logged-in customers can use the basket
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/sudoHome";

        basketService.decrementItem(id);
        return "redirect:/basket";
    }

    // shows the payment screen (fake checkout basically)
    @GetMapping("/payment")
    public String showPayment(HttpSession session, Model model) {
        // only logged-in customers can pay
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/sudoHome";

        model.addAttribute("total", basketService.getTotalPrice());
        return "payment";
    }

    @PostMapping("/payment/confirm")
    public String confirmPayment(HttpSession session) {
        // grab user from session (if it's gone, we're back to login)
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        String username = user.getUsername();

        // basket snapshot at checkout time
        var content = basketService.getBasketContent();
        double total = basketService.getTotalPrice();

        if (content.isEmpty()) {
            return "redirect:/basket";
        }

        // write the order to h2, then clear the basket
        orderService.saveOrder(username, content, total);
        basketService.clear();

        return "redirect:/home";
    }

}