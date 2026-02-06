package org.example.marketplace.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasketController {

    @GetMapping("/basket")
    public String basket() {
        return "basket";
    }

    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }
}