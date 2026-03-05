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

    // login page is the default landing page
    @GetMapping({"/","/login"})
    public String login() {
        return "login";
    }

    // handle login: redirect based on role, stash user in session
    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        // check creds against the db
        User user = authService.authenticate(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            // stash the user object in the session
            session.setAttribute("user", user);

            // send admins and users to their own home
            if ("admin".equalsIgnoreCase(user.getRole())) {
                return "redirect:/sudoHome";
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
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String role) {
        // check if username is already taken
        if (authService.userExists(username)) {
            return "redirect:/register?error=exists";
        }
        // save the new user to h2
        authService.registerUser(username, password, role);
        return "redirect:/login?registered=true";
    }

    // nuke the session and send back to login
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}