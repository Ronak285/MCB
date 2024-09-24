package com.mcb.ecommerce.order_management.repository;

import com.mcb.ecommerce.order_management.model.Order;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;

@Repository
public class OrderRepository {
    private final Map<UUID, Order> orders = new HashMap<>();

    public Order save(Order order) {
        orders.put(order.getOrderId(), order);
        return order;
    }

    public Optional<Order> findById(UUID orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public void deleteById(UUID orderId) {
        orders.remove(orderId);
    }

    // Method to find orders by customer name
    public List<Order> findByCustomerName(String customerName) {
        return orders.values().stream()
                .filter(order -> order.getCustomerName().equals(customerName))
                .toList();
    }

}

