package com.example.marketplace.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SudoOrderController {

    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }

    @GetMapping("/editOrder")
    public String editOrder() {
        return "editOrder";
    }
}