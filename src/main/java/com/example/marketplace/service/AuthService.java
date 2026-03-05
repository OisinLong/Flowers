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
        // dead simple check for now (no hashing yet)
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // quick guard for registration
    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    // saves a new user row
    public void registerUser(String username, String password, String role) {
        User user = new User(username, password, role);
        userRepository.save(user);
    }

    // checks current password before letting u swap it
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