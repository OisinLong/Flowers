package com.example.marketplace.repository;

import com.example.marketplace.model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    // load all favourites for a user
    List<Favourite> findByUsername(String username);

    // used to show the filled heart on item.html
    Favourite findByUsernameAndProduct_Id(String username, Long productId);

    // unfavourite
    @Transactional
    void deleteByUsernameAndProduct_Id(String username, Long productId);
}
