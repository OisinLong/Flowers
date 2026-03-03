package com.example.marketplace.service;

import com.example.marketplace.model.User;
import com.example.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        // Basic credential check
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Check if a username is already taken
    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    // Save a new user to the database
    public void registerUser(String username, String password, String role) {
        User user = new User(username, password, role);
        userRepository.save(user);
    }

    // Verify the current password and update to the new one
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(currentPassword)) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}