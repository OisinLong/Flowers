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
}