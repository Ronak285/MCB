package com.mcb.ecommerce.order_management.dto.request;

import com.mcb.ecommerce.order_management.model.OrderStatus;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderUpdateRequest {

    @Pattern(regexp = "PENDING|COMPLETED|CANCELLED", message = "Invalid order status, must be one of PENDING, COMPLETED, or CANCELLED")
    private OrderStatus status;
}