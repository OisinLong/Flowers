package com.example.marketplace.repository;

import com.example.marketplace.model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    // Load all favourites for a given user
    List<Favourite> findByUsername(String username);

    // Check if a specific product is already favourited by a user
    Favourite findByUsernameAndProduct_Id(String username, Long productId);

    // Remove a favourite by username and product id
    @Transactional
    void deleteByUsernameAndProduct_Id(String username, Long productId);
}

