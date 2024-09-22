package com.mcb.ecommerce.order_management.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Order {
    private UUID orderId;
    private String customerName;
    private String productName;
    private int quantity;
    private double price;
    private OrderStatus status;
    private LocalDateTime dateCreated;

    public Order() {
    }

    public Order(String customerName, String productName, int quantity, double price) {
        this.orderId = UUID.randomUUID();
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.status = OrderStatus.PENDING;
        this.dateCreated = LocalDateTime.now();
    }
}