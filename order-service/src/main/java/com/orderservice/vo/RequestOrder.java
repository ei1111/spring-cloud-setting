package com.orderservice.vo;

import com.orderservice.dto.OrderDto;
import lombok.Data;

@Data
public class RequestOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;


    public OrderDto toEntity(String userId) {
        return  OrderDto.builder()
                .userId(userId)
                .productId(productId)
                .qty(qty)
                .unitPrice(unitPrice)
                .build();
    }
}
