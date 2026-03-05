package com.example.marketplace.service;

import com.example.marketplace.model.Order;
import com.example.marketplace.model.OrderItem;
import com.example.marketplace.model.Product;
import com.example.marketplace.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /* turns the session basket into real order rows in h2 */
    @Transactional
    public void saveOrder(String username, Map<Product, Integer> basketContent, double total) {
        // 1. order header (high level stuff)
        Order order = new Order();
        order.setUsername(username);
        double roundedTotal = Math.round(total * 100.0) / 100.0; // keep it tidy for display
        order.setTotalAmount(roundedTotal);
        order.setStatus("Processing");
        order.setOrderDate(LocalDateTime.now());

        // 2. snapshot each basket line into an OrderItem (so history doesn't change later)
        for (Map.Entry<Product, Integer> entry : basketContent.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            OrderItem item = new OrderItem();
            item.setProductName(product.getName());
            item.setPriceAtPurchase(product.getPrice()); // freeze the price at checkout time
            item.setQuantity(quantity);

            // 3. add the item under the order; cascade takes care of saving it
            order.getItems().add(item);
        }

        // 4. save once; @Transactional keeps it all as one unit
        orderRepository.save(order);
    }

    /* user order history */
    public List<Order> findOrdersByUsername(String username) {
        return orderRepository.findByUsername(username);
    }

    /* admin view of everything */
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    /* grabs only orders that match a status string */
    public List<Order> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    /* flips the order status and persists it */
    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(newStatus);
        orderRepository.save(order);
    }
}