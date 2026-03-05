package com.example.marketplace.repository;

import com.example.marketplace.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // user order history
    List<Order> findByUsername(String username);

    // used to split processing vs delivered pages
    List<Order> findByStatus(String status);
}