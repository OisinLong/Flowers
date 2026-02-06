package org.example.marketplace.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/ordersUser")
    public String orders() {
        return "ordersUser";
    }

    @GetMapping("/item")
    public String item() {
        return "item";
    }
}