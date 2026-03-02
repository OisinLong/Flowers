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

    /**
     * Transforms the session basket into a permanent database record.
     * @Transactional ensures that the Order and all OrderItems are saved as a single unit.
     */
    @Transactional
    public void saveOrder(String username, Map<Product, Integer> basketContent, double total) {
        // 1. Create the main Order header
        Order order = new Order();
        order.setUsername(username);
        double roundedTotal = Math.round(total * 100.0) / 100.0; // Round to 2 decimal places
        order.setTotalAmount(roundedTotal);
        order.setStatus("Processing");
        order.setOrderDate(LocalDateTime.now());

        // 2. Convert each Product in the basket into a permanent OrderItem
        for (Map.Entry<Product, Integer> entry : basketContent.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            OrderItem item = new OrderItem();
            item.setProductName(product.getName());
            item.setPriceAtPurchase(product.getPrice()); // Snapshot the price
            item.setQuantity(quantity);

            // 3. Link the item to the order (establishing the One-to-Many relationship)
            order.getItems().add(item);
        }

        // 4. Save to H2. CascadeType.ALL in the Order entity will automatically save the items.
        orderRepository.save(order);
    }

    /**
     * Used by the User profile to see their specific past purchases.
     */
    public List<Order> findOrdersByUsername(String username) {
        // We'll use the custom method we added to the OrderRepository
        return orderRepository.findByUsername(username);
    }

    /**
     * Used by the Admin screen to see the entire history of the marketplace.
     */
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Returns only orders with the given status (e.g. "Processing" or "Delivered").
     */
    public List<Order> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * Updates the status of a single order and persists the change to the DB.
     */
    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(newStatus);
        orderRepository.save(order);
    }
}