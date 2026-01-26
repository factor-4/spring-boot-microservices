package com.spring_ecom.order.dtos;


import com.spring_ecom.order.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String userId;
    private OrderStatus status;
    private List<OrderItemDto> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
