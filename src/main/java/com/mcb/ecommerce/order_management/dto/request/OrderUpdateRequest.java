package com.mcb.ecommerce.order_management.dto.request;

import com.mcb.ecommerce.order_management.model.OrderStatus;
import lombok.Data;

@Data
public class OrderUpdateRequest {
    private OrderStatus status;
}