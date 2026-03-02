package com.example.marketplace.repository;

import com.example.marketplace.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    // Filter products between a min and max price, sorted ascending
    @Query("SELECT p FROM Product p WHERE p.price >= :min AND p.price <= :max ORDER BY p.price ASC")
    List<Product> findByPriceBetweenAsc(@Param("min") double min, @Param("max") double max);

    // Filter products between a min and max price, sorted descending
    @Query("SELECT p FROM Product p WHERE p.price >= :min AND p.price <= :max ORDER BY p.price DESC")
    List<Product> findByPriceBetweenDesc(@Param("min") double min, @Param("max") double max);
}