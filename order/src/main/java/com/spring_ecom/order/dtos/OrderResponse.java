package com.spring_ecom.order.dtos;

import com.spring_ecom.order.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus staus;
    private List<OrderItemDto> items;
    private LocalDateTime createdAt;
}
