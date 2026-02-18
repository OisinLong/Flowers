package com.example.marketplace.controller;

import com.example.marketplace.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @GetMapping
    public String viewBasket(Model model) {
        // The Controller just asks the Service for data and passes it to the View
        model.addAttribute("items", basketService.getBasketContent());
        model.addAttribute("total", basketService.getTotalPrice());
        return "basket";
    }

    @PostMapping("/add/{id}")
    public String addToBasket(@PathVariable Long id) {
        basketService.addItem(id);
        return "redirect:/basket";
    }

    @PostMapping("/remove/{id}")
    public String removeFromBasket(@PathVariable Long id) {
        basketService.removeItem(id);
        return "redirect:/basket";
    }

    @PostMapping("/increment/{id}")
    public String increment(@PathVariable Long id) {
        basketService.incrementItem(id);
        return "redirect:/basket";
    }

    @PostMapping("/decrement/{id}")
    public String decrement(@PathVariable Long id) {
        basketService.decrementItem(id);
        return "redirect:/basket";
    }
}