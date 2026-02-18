package com.example.marketplace.controller;

import com.example.marketplace.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BasketController {

    @Autowired
    private BasketService basketService;

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

}