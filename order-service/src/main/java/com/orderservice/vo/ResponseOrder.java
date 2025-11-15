package com.orderservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orderservice.jpa.OrderEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;

    public static ResponseOrder from(OrderEntity orderEntity) {
        return ResponseOrder.builder()
                .orderId(orderEntity.getOrderId())
                .qty(orderEntity.getQty())
                .unitPrice(orderEntity.getUnitPrice())
                .totalPrice(orderEntity.getTotalPrice())
                .createdAt(orderEntity.getCreatedAt())
                .build();
    }
}