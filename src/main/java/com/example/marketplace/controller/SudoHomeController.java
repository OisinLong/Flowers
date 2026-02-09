package com.example.marketplace.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SudoHomeController {

    @GetMapping("/sudoHome")
    public String sudoHome() {
        return "sudoHome";
    }

    @GetMapping("/orderHistory")
    public String orderHistory() {
        return "orderHistory";
    }

    @GetMapping("/editItem")
    public String editItem() {
        return "editItem";
    }
}