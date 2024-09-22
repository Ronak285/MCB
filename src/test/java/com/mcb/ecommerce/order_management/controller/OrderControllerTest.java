package com.mcb.ecommerce.order_management.controller;

import com.mcb.ecommerce.order_management.dto.request.OrderRequest;
import com.mcb.ecommerce.order_management.dto.request.OrderUpdateRequest;
import com.mcb.ecommerce.order_management.model.Order;
import com.mcb.ecommerce.order_management.model.OrderStatus;
import com.mcb.ecommerce.order_management.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private OrderRequest validOrderRequest;
    private Order order;
    private OrderUpdateRequest updateRequest;
    private UUID orderId;
    private static final String CUSTOMER_NAME = "ABC XYZ";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        Field serviceField = OrderController.class.getDeclaredField("orderService");
        serviceField.setAccessible(true);
        serviceField.set(orderController, orderService);

        validOrderRequest = new OrderRequest();
        validOrderRequest.setCustomerName(CUSTOMER_NAME);
        validOrderRequest.setProductName("Laptop");
        validOrderRequest.setQuantity(1);
        validOrderRequest.setPrice(1500.00);

        orderId = UUID.randomUUID();
        order = new Order();
        order.setCustomerName(CUSTOMER_NAME);
        order.setProductName("Laptop");
        order.setQuantity(1);
        order.setPrice(1500.00);
        order.setStatus(OrderStatus.PENDING);

        updateRequest = new OrderUpdateRequest();
        updateRequest.setStatus(OrderStatus.COMPLETED); // Use direct enum value
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() {
        when(orderService.createOrder(validOrderRequest)).thenReturn(order);

        ResponseEntity<Order> response = orderController.createOrder(validOrderRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderService, times(1)).createOrder(validOrderRequest);
    }

    @Test
    void getOrders_ShouldReturnListOfOrders() {
        when(orderService.getOrders(null, null, null)).thenReturn(Collections.singletonList(order));

        ResponseEntity<List<Order>> response = orderController.getOrders(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(orderService, times(1)).getOrders(null, null, null);
    }

    @Test
    void getOrders_ShouldReturnFilteredOrdersByCustomerName() {
        when(orderService.getOrders("ABC XYZ", null, null)).thenReturn(Collections.singletonList(order));

        ResponseEntity<List<Order>> response = orderController.getOrders("ABC XYZ", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(orderService, times(1)).getOrders("ABC XYZ", null, null);
    }

    @Test
    void getOrders_ShouldReturnFilteredOrdersByStatus() {
        when(orderService.getOrders(null, "PENDING", null)).thenReturn(Collections.singletonList(order));

        ResponseEntity<List<Order>> response = orderController.getOrders(null, "PENDING", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(orderService, times(1)).getOrders(null, "PENDING", null);
    }

    @Test
    void getOrderById_ShouldReturnOrder() {
        when(orderService.getOrderById(orderId)).thenReturn(order);

        ResponseEntity<Order> response = orderController.getOrderById(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrder() {
        when(orderService.updateOrder(orderId, updateRequest)).thenReturn(order);

        ResponseEntity<Order> response = orderController.updateOrder(orderId, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderService, times(1)).updateOrder(orderId, updateRequest);
    }

    @Test
    void deleteOrder_ShouldReturnNoContent() {
        doNothing().when(orderService).deleteOrder(orderId);

        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderService, times(1)).deleteOrder(orderId);
    }

    @Test
    void getOrderHistory_ShouldReturnListOfOrders() {
        when(orderService.getOrderHistory("ABC XYZ")).thenReturn(Collections.singletonList(order));

        ResponseEntity<List<Order>> response = orderController.getOrderHistory("ABC XYZ");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(orderService, times(1)).getOrderHistory("ABC XYZ");
    }
}
