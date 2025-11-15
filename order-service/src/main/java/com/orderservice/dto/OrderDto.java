package com.orderservice.dto;

import com.orderservice.jpa.OrderEntity;
import com.orderservice.vo.ResponseOrder;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String orderId;
    private String userId;

    public OrderEntity toEntity() {
        return OrderEntity.builder()
                .productId(productId)
                .qty(qty)
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .orderId(UUID.randomUUID().toString())
                .userId(userId)
                .totalPrice(getTotalPrice())
                .build();
    }


    public ResponseOrder toResponse() {
        return ResponseOrder.builder()
                .productId(productId)
                .qty(qty)
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .orderId(orderId)
                .build();
    }

    private int getTotalPrice() {
        return qty * unitPrice;
    }
}