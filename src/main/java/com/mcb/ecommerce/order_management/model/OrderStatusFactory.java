package com.mcb.ecommerce.order_management.model;

public class OrderStatusFactory {
    public static OrderStatus createOrderStatus(String status) {
        return switch (status.toUpperCase()) {
            case "PENDING" -> OrderStatus.PENDING;
            case "COMPLETED" -> OrderStatus.COMPLETED;
            case "CANCELLED" -> OrderStatus.CANCELLED;
            default -> throw new IllegalArgumentException("Unknown status");
        };
    }
}
