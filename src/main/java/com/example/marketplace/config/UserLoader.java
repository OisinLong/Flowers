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

        User normalUser1 = new User();
        normalUser1.setUsername("oisin");
        normalUser1.setPassword("user123");
        normalUser1.setRole("USER");

        User normalUser2 = new User();
        normalUser2.setUsername("james");
        normalUser2.setPassword("password");
        normalUser2.setRole("USER");

        User normalUser3 = new User();
        normalUser3.setUsername("rory");
        normalUser3.setPassword("terenure");
        normalUser3.setRole("USER");

        userRepo.save(normalUser1);
        userRepo.save(normalUser2);
        userRepo.save(normalUser3);

        User adminUser1 = new User();
        adminUser1.setUsername("admin");
        adminUser1.setPassword("admin123");
        adminUser1.setRole("ADMIN");

        User adminUser2 = new User();
        adminUser2.setUsername("sudo");
        adminUser2.setPassword("sudo123");
        adminUser2.setRole("ADMIN");

        userRepo.save(adminUser1);
        userRepo.save(adminUser2);

        System.out.println("UserLoader: Initial users successfully loaded into H2.");
    }
}