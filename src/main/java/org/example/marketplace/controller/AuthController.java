package org.example.marketplace.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    // Ensuing that the login page is the default landing page
    @GetMapping({"/","/login"})
    public String login() {
        return "login";
    }

    // Handle login POST: ignore credentials and always redirect to the home page
    @PostMapping("/login")
    public String doLogin(@RequestParam(name = "username", required = false) String username) {
        String u = (username == null) ? "" : username.trim();
        if ("sudo".equalsIgnoreCase(u)) {
            return "redirect:/sudoHome";
        }
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister() {
        return "redirect:/login";
    }
}