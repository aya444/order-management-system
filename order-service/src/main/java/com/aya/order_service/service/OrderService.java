package com.aya.order_service.service;

import com.aya.order_service.dto.OrderDto;
import com.aya.order_service.dto.OrderInputDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderInputDto orderInputDto);

    void completeOrder(Integer id);

    void cancelOrder(Integer id);

    List<OrderDto> getOrdersByCustomerId(Integer customerId);
}
