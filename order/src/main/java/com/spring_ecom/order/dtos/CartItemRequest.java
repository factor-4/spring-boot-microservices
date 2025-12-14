package com.spring_ecom.order.dtos;


import lombok.Data;

@Data
public class CartItemRequest {
    private String ProductId;
    private Integer quantity;
}
