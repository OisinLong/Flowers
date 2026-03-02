package com.example.marketplace.repository;

import com.example.marketplace.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // This allows you to show a specific user their history
    List<Order> findByUsername(String username);

    // Filter all orders by a given status (e.g. "Processing" or "Delivered")
    List<Order> findByStatus(String status);
}