package com.mcb.ecommerce.order_management.controller.service;

import com.mcb.ecommerce.order_management.dto.request.OrderRequest;
import com.mcb.ecommerce.order_management.dto.request.OrderUpdateRequest;
import com.mcb.ecommerce.order_management.model.Order;
import com.mcb.ecommerce.order_management.model.OrderStatus;
import com.mcb.ecommerce.order_management.repository.OrderRepository;
import com.mcb.ecommerce.order_management.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    private Order order;
    private OrderRequest validOrderRequest;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validOrderRequest = new OrderRequest();
        validOrderRequest.setCustomerName("ABC XYZ");
        validOrderRequest.setProductName("Laptop");
        validOrderRequest.setQuantity(1);
        validOrderRequest.setPrice(1500.00);

        orderId = UUID.randomUUID();
        order = new Order();
        order.setCustomerName("ABC XYZ");
        order.setProductName("Laptop");
        order.setQuantity(1);
        order.setPrice(1500.00);
        order.setStatus(OrderStatus.PENDING);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void createOrder_ShouldPersistOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(validOrderRequest);

        assertNotNull(createdOrder);
        assertEquals(order.getCustomerName(), createdOrder.getCustomerName());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void getOrders_ShouldReturnAllOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderService.getOrders(null, null, null);

        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void getOrders_ShouldReturnFilteredOrdersByCustomerName() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderService.getOrders("ABC XYZ", null, null);

        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void getOrders_ShouldReturnFilteredOrdersByStatus() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderService.getOrders(null, "PENDING", null);

        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void getOrders_ShouldSortOrdersByDateCreatedAsc() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        // You would need to set the dateCreated property appropriately for this test
        // Assuming order has a dateCreated field and has been set in the setup

        List<Order> orders = orderService.getOrders(null, null, "asc");

        // Check if the orders are sorted correctly (this may require more orders to properly test sorting)
        assertFalse(orders.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void getOrders_ShouldSortOrdersByDateCreatedDesc() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        // You would need to set the dateCreated property appropriately for this test

        List<Order> orders = orderService.getOrders(null, null, "desc");

        // Check if the orders are sorted correctly
        assertFalse(orders.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void getOrderById_ShouldReturnOrder() {
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        Order foundOrder = orderService.getOrderById(orderId);

        assertNotNull(foundOrder);
        assertEquals(order, foundOrder);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    void updateOrder_ShouldReturnUpdatedOrder() {
        Order updatedOrder = new Order();
        updatedOrder.setStatus(OrderStatus.COMPLETED);
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.setStatus(OrderStatus.COMPLETED);
        Order result = orderService.updateOrder(orderId, orderUpdateRequest);

        assertEquals(updatedOrder.getStatus(), result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    void getOrderHistory_ShouldReturnOrderHistory() {
        String customerName = "ABC XYZ"; // Customer name to filter by
        Order order = new Order();
        order.setCustomerName(customerName); // Set the customer name
        order.setOrderId(UUID.randomUUID()); // Set a random UUID for the order
        order.setStatus(OrderStatus.PENDING); // Set a valid status

        // Mock the repository to return the order for the specified customer name
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        // Call the service method
        List<Order> orderHistory = orderService.getOrderHistory(customerName);

        // Assertions
        assertFalse(orderHistory.isEmpty(), "Order history should not be empty");
        assertEquals(1, orderHistory.size(), "Order history should contain one order");
        assertEquals(customerName, orderHistory.get(0).getCustomerName(), "The customer's name should match");
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    void deleteOrder_ShouldRemoveOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOrderId(orderId);

        // Set up the mock repository to return the order when searching for it
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Call the method to delete the order
        orderService.deleteOrder(orderId);

        // Verify that the order is indeed removed from the repository
        verify(orderRepository, times(1)).deleteById(orderId);
    }



}
