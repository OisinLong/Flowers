package com.example.marketplace.repository;

import com.example.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // This allows the service to fetch a user by their username
    User findByUsername(String username);
}