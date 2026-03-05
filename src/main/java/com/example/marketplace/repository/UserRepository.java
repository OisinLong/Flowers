package com.example.marketplace.repository;

import com.example.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // used by auth to fetch a user by username
    User findByUsername(String username);
}