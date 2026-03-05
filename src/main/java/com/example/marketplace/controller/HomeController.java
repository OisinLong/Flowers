package com.example.marketplace.controller;
import com.example.marketplace.model.Favourite;
import com.example.marketplace.model.Order;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.FavouriteRepository;
import com.example.marketplace.repository.OrderRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.service.AuthService;
import com.example.marketplace.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final FavouriteRepository favouriteRepository;
    private final AuthService authService;

    public HomeController(ProductRepository productRepository,
                          OrderRepository orderRepository,
                          OrderService orderService,
                          FavouriteRepository favouriteRepository,
                          AuthService authService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.favouriteRepository = favouriteRepository;
        this.authService = authService;
    }

    @GetMapping("/home")
    public String home(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "asc") String sort,
            HttpSession session,
            Model model) {

        // kick admins over to their own home
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";
        if ("admin".equalsIgnoreCase(sessionUser.getRole())) return "redirect:/sudoHome";

        // grab overall price bounds for the slider
        List<Product> all = productRepository.findAll();
        double overallMin = all.stream().mapToDouble(Product::getPrice).min().orElse(0);
        double overallMax = all.stream().mapToDouble(Product::getPrice).max().orElse(1000);

        // fall back to full range if nothing supplied
        if (minPrice == null) minPrice = overallMin;
        if (maxPrice == null) maxPrice = overallMax;

        // filtered + sorted products
        List<Product> products;
        if ("desc".equals(sort)) {
            products = productRepository.findByPriceBetweenDesc(minPrice, maxPrice);
        } else {
            products = productRepository.findByPriceBetweenAsc(minPrice, maxPrice);
        }

        model.addAttribute("products", products);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("overallMin", overallMin);
        model.addAttribute("overallMax", overallMax);
        model.addAttribute("sort", sort);

        return "home";
    }

    @GetMapping("/ordersUser")
    public String viewUserOrders(HttpSession session, Model model) {
        // pull logged-in user from session
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        // admins go to their own order view
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/orders";

        // only this user's orders
        List<Order> userOrders = orderService.findOrdersByUsername(user.getUsername());

        // push to the template
        model.addAttribute("orders", userOrders);

        return "ordersUser";
    }

    @GetMapping("/item/{id}")
    public String item(@PathVariable Long id, HttpSession session, Model model) {
        // gotta be logged in
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);

        // check if this user already favourited the product
        boolean isFavourited = favouriteRepository.findByUsernameAndProduct_Id(user.getUsername(), id) != null;
        model.addAttribute("isFavourited", isFavourited);

        return "item";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        // admins have their own profile
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/sudoProfile";

        String username = user.getUsername();

        double totalSpend = orderRepository.findByUsername(username).stream()
                .map(o -> o.getTotalAmount() == null ? 0.0 : o.getTotalAmount())
                .reduce(0.0, Double::sum);

        // derive a simple status from spend
        String status = totalSpend > 0 ? "Active" : "New";

        model.addAttribute("username", username);
        model.addAttribute("status", status);
        model.addAttribute("totalSpend", totalSpend);

        // favourites for the horizontal scroller
        List<Favourite> favourites = favouriteRepository.findByUsername(username);
        model.addAttribute("favourites", favourites);

        return "profile";
    }

    // serves the change password page
    @GetMapping("/changePassword")
    public String showChangePassword(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/sudoChangePassword";
        return "changePassword";
    }

    // handles the password change form
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("admin".equalsIgnoreCase(user.getRole())) return "redirect:/sudoChangePassword";

        // new password and confirm gotta match
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/changePassword?pwError=mismatch";
        }

        // fire the change through AuthService
        boolean changed = authService.changePassword(user.getUsername(), currentPassword, newPassword);
        if (changed) {
            return "redirect:/changePassword?pwSuccess=true";
        }
        return "redirect:/changePassword?pwError=wrong";
    }

}