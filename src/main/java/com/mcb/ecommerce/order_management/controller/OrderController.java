package com.mcb.ecommerce.order_management.controller;

import com.mcb.ecommerce.order_management.dto.request.OrderRequest;
import com.mcb.ecommerce.order_management.dto.request.OrderUpdateRequest;
import com.mcb.ecommerce.order_management.model.Order;
import com.mcb.ecommerce.order_management.repository.OrderRepository;
import com.mcb.ecommerce.order_management.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing orders in the e-commerce system.
 */
@Tag(name = "Order Management", description = "APIs for managing orders in the e-commerce system")
@RestController
@RequestMapping("/api/v1/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderService = OrderService.getInstance(orderRepository); // Use Singleton pattern
    }

    /**
     * Creates a new order.
     * @param orderRequest the details of the order to create.
     * @return the created order.
     */
    @Operation(summary = "Create a new Order", description = "Creates a new order (Admin Only).")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * Retrieves a list of all orders.
     * @param customerName the name of the customer.
     * @param sort sorting order (ascending or descending).
     * @param status filter orders by status.
     * @return a list of orders.
     */
    @Operation(summary = "Get all Orders", description = "Retrieve a list of all orders with optional sorting and filtering by customerName or status.")
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestParam(required = false) String customerName,
                                                 @RequestParam(required = false) String status,
                                                 @RequestParam(required = false) String sort) {
        List<Order> orders = orderService.getOrders(customerName, status, sort);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves an order by its ID.
     * @param id the ID of the order.
     * @return the order with the specified ID.
     */
    @Operation(summary = "Get Order by ID", description = "Retrieve an order by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Updates an order's status.
     * @param id the ID of the order to update.
     * @param updateRequest the new status.
     * @return the updated order.
     */
    @Operation(summary = "Update Order Status", description = "Update the status of an order (Admin Only).")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrder(@PathVariable UUID id,
                                             @RequestBody OrderUpdateRequest updateRequest) {
        Order updatedOrder = orderService.updateOrder(id, updateRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Deletes an order.
     * @param id the ID of the order to delete.
     * @return no content.
     */
    @Operation(summary = "Delete an Order", description = "Delete an order by its ID (Admin Only).")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the order history for a specific customer.
     * @param customerName the name of the customer.
     * @return the order history for the specified customer.
     */
    @Operation(summary = "Get Order History for a Customer", description = "Retrieve the order history for a customer.")
    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable String customerName) {
        List<Order> orders = orderService.getOrderHistory(customerName);
        return ResponseEntity.ok(orders);
    }
}
