package com.orderservice.service;

import com.orderservice.dto.OrderDto;
import com.orderservice.jpa.OrderEntity;
import com.orderservice.jpa.OrderRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        OrderEntity orderEntity = orderDto.toEntity();
        OrderEntity result = orderRepository.save(orderEntity);
        return result.toResponse();
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);

        return orderEntity.toResponse();
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
