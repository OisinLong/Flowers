package com.example.marketplace.controller;
import com.example.marketplace.model.User;
import com.example.marketplace.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

    // Ensuing that the login page is the default landing page
    @GetMapping({"/","/login"})
    public String login() {
        return "login";
    }

    // Handle login POST: ignore credentials and always redirect to the home page
    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        // Checking the user against the database and authenticating them
        User user = authService.authenticate(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            // Storing the user object in the session
            session.setAttribute("user", user);

            // Checking if the user is an admin or normal user
            if ("admin".equalsIgnoreCase(user.getRole())) {
                return "redirect:/adminHome";
            } else {
                return "redirect:/home";
            }
        }
        return "redirect:/login?error=true";
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