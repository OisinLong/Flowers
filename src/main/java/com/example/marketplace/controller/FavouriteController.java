package com.example.marketplace.controller;

import com.example.marketplace.model.Favourite;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.FavouriteRepository;
import com.example.marketplace.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FavouriteController {

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private ProductRepository productRepository;

    // Toggle favourite on/off — used by the heart button on the item page
    @PostMapping("/favourites/toggle/{id}")
    public String toggleFavourite(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        String username = user.getUsername();
        Favourite existing = favouriteRepository.findByUsernameAndProduct_Id(username, id);

        if (existing == null) {
            // Not yet favourited — add it
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            favouriteRepository.save(new Favourite(username, product));
        } else {
            // Already favourited — remove it
            favouriteRepository.deleteByUsernameAndProduct_Id(username, id);
        }

        return "redirect:/item/" + id;
    }

    // Remove favourite — used by the ✕ button on the profile scroller
    @PostMapping("/favourites/remove/{id}")
    public String removeFavourite(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        favouriteRepository.deleteByUsernameAndProduct_Id(user.getUsername(), id);
        return "redirect:/profile";
    }
}

