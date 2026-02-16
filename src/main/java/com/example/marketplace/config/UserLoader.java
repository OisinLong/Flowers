package com.example.marketplace.config;

import com.example.marketplace.model.User;
import com.example.marketplace.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class UserLoader {

    private final UserRepository userRepo;

    public UserLoader(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Automatically runs on startup to populate users in the H2 database.
     */
    @PostConstruct
    public void loadUserData() {
        // Prevent duplicate users if the file-based database already exists
        if (userRepo.count() > 0) {
            return;
        }

        User normalUser = new User();
        normalUser.setUsername("oisin");
        normalUser.setPassword("user123");
        normalUser.setRole("USER");

        userRepo.save(normalUser);

        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("admin123");
        adminUser.setRole("ADMIN");

        userRepo.save(adminUser);

        System.out.println("UserLoader: Initial users successfully loaded into H2.");
    }
}