package com.mcb.ecommerce.order_management.service;

import com.mcb.ecommerce.order_management.dto.request.OrderRequest;
import com.mcb.ecommerce.order_management.dto.request.OrderUpdateRequest;
import com.mcb.ecommerce.order_management.exception.OrderNotFoundException;
import com.mcb.ecommerce.order_management.model.Order;
import com.mcb.ecommerce.order_management.model.OrderStatus;
import com.mcb.ecommerce.order_management.model.OrderStatusFactory;
import com.mcb.ecommerce.order_management.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling the business logic related to Orders.
 * It provides methods to perform CRUD operations, search, and sorting of orders.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PriorityQueue<Order> orderQueue;

    // Singleton instance
    private static volatile OrderService instance;

    private OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.orderQueue = new PriorityQueue<>(Comparator.comparing(Order::getDateCreated));
    }

    /**
     * Returns the singleton instance of OrderService using double-checked locking.
     * This ensures that the instance is created only once in a thread-safe manner.
     * @param repo the OrderRepository for database operations.
     * @return Singleton instance of OrderService.
     */
    public static OrderService getInstance(OrderRepository repo) {
        if (instance == null) {
            synchronized (OrderService.class) {
                if (instance == null) {
                    instance = new OrderService(repo);
                }
            }
        }
        return instance;
    }

    /**
     * Creates a new order with the provided details.
     * @param orderRequest contains the details for creating the new order.
     * @return the newly created Order.
     * @throws IllegalArgumentException if quantity or price is less than or equal to zero.
     */
    public Order createOrder(OrderRequest orderRequest) {
        Order order = new Order(orderRequest.getCustomerName(),
                orderRequest.getProductName(),
                orderRequest.getQuantity(),
                orderRequest.getPrice()
        );

        orderRepository.save(order);
        orderQueue.offer(order);

        return order;
    }

    /**
     * Retrieves all orders, optionally filtering by status and sorting by creation date.
     * @param sort specifies the sorting order, "asc" for ascending or "desc" for descending.
     * @param status filter by order status (e.g., Pending, Completed).
     * @return a list of orders matching the criteria.
     */
    public List<Order> getOrders(String customerName, String status, String sort) {
        List<Order> orders = orderRepository.findAll();

        if (status != null) {
            OrderStatus orderStatus = OrderStatusFactory.createOrderStatus(status);
            orders = orders.stream()
                    .filter(order -> order.getStatus().equals(orderStatus))
                    .toList();
        }

        if (customerName != null) {
            orders = orders.stream()
                    .filter(order -> order.getCustomerName().equalsIgnoreCase(customerName))
                    .toList();
        }

        // Sorting logic
        if ("desc".equalsIgnoreCase(sort)) {
            orders.sort(Comparator.comparing(Order::getDateCreated).reversed());
        } else if ("asc".equalsIgnoreCase(sort)) {
            orders.sort(Comparator.comparing(Order::getDateCreated));
        }

        return orders;
    }


    /**
     * Retrieves a specific order by its ID.
     * @param orderId the unique identifier of the order.
     * @return the order if found.
     * @throws OrderNotFoundException if no order is found with the provided ID.
     */
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    }

    /**
     * Updates the status of an existing order.
     * @param orderId the unique identifier of the order.
     * @param updateRequest contains the new status for the order.
     * @return the updated order.
     * @throws OrderNotFoundException if no order is found with the provided ID.
     */
    public Order updateOrder(UUID orderId, OrderUpdateRequest updateRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        OrderStatus newStatus = OrderStatusFactory.createOrderStatus(updateRequest.getStatus().toString());
        order.setStatus(newStatus);

        orderRepository.save(order);
        orderQueue.remove(order);
        orderQueue.offer(order);

        return order;
    }

    /**
     * Deletes an order by its ID.
     * @param orderId the unique identifier of the order.
     * @throws OrderNotFoundException if no order is found with the provided ID.
     */
    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        orderRepository.deleteById(orderId);
        orderQueue.remove(order);
    }

    /**
     * Retrieves the order history for a specific customer.
     * @param customerName the name of the customer whose order history is being retrieved.
     * @return a list of orders belonging to the specified customer.
     */
    public List<Order> getOrderHistory(String customerName) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomerName().equalsIgnoreCase(customerName))
                .toList();
    }
}
