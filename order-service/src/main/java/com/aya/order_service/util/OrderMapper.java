package com.aya.order_service.util;

import com.aya.order_service.dto.OrderDto;
import com.aya.order_service.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order fromDtoToOrder(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.getId())
                .customerId(orderDto.getCustomerId())
                .totalPrice(orderDto.getTotalPrice())
                .createdAt(orderDto.getCreatedAt())
                .updatedAt(orderDto.getUpdatedAt())
                .status(orderDto.getStatus())
                .productsIds(orderDto.getProductsIds())
                .build();
    }

    public OrderDto fromOrderToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .status(order.getStatus())
                .productsIds(order.getProductsIds())
                .build();
    }
}
