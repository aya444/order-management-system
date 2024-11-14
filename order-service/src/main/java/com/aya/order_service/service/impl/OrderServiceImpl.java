package com.aya.order_service.service.impl;


import com.aya.order_service.dto.OrderDto;
import com.aya.order_service.dto.OrderInputDto;
import com.aya.order_service.entity.Order;
import com.aya.order_service.entity.OrderStatus;
import com.aya.order_service.dto.ProductInputDto;
import com.aya.order_service.dto.ProductDto;
import com.aya.order_service.exception.InvalidOrderDataException;
import com.aya.order_service.exception.NoOrderFoundException;
import com.aya.order_service.exception.NoProductFoundException;
import com.aya.order_service.feign.InventoryClient;
import com.aya.order_service.repository.OrderRepository;
import com.aya.order_service.service.OrderService;
import com.aya.order_service.util.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final OrderMapper orderMapper;


    public OrderDto createOrder(OrderInputDto orderInputDto) {
        if(orderInputDto == null){
            throw new InvalidOrderDataException("Order data cannot be null");
        }

        // Convert ProductInputDto to ProductDto
        List<ProductInputDto> productInputDtos = orderInputDto.getProductInputDtoIds().stream()
                .map(productId -> new ProductInputDto(productId.getId(), productId.getQuantity()))
                .toList();

        if (productInputDtos.isEmpty()) {
            throw new NoProductFoundException("No product found for the given product IDs");
        }

        // Fetch product Ids
        List<Integer> productIds = productInputDtos.stream()
                .map(ProductInputDto::getId)
                .toList();

        // Fetch products from inventory service
        List<ProductDto> productDtoList = productIds.stream()
                .map(product -> inventoryClient
                        .getProductById(product)
                        .getBody())
                .filter(Objects::nonNull)
                .toList();

        // Calculate total amount
        double totalAmount = productDtoList.stream()
                .map(product -> {
                    Optional<Double> price = Optional.ofNullable(inventoryClient.getProductPriceById(product.getId()).getBody());
                    return price.map(p -> p * product.getQuantity()).orElse(0.0);
                })
                .mapToDouble(Double::doubleValue)
                .sum();

        // Deduct stock from inventory service and check quantity validity
        productInputDtos.forEach(productInputDto -> inventoryClient.deductProductFromStock(productInputDto.getId(), productInputDto.getQuantity()));

        // Create order
        Order order = Order.builder()
                .customerId(orderInputDto.getCustomerId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalPrice(totalAmount)
                .status(OrderStatus.PENDING)
                .productsIds(productIds)
                .build();

        orderRepository.save(order);
        return orderMapper.fromOrderToDto(order);
    }

    public void completeOrder(Integer id) {
        if(id == null){
            throw new InvalidOrderDataException("Order id cannot be null");
        }

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoOrderFoundException("Order not found"));

        order.setStatus(OrderStatus.COMPLETED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        // Send message to Kafka
//        kafkaTemplate.send("order-completed", "Order completed: " + orderId);

    }



    public void cancelOrder(Integer id) {
        if(id == null){
            throw new InvalidOrderDataException("Order id cannot be null");
        }

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoOrderFoundException("Order not found"));

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // Send message to Kafka
//        kafkaTemplate.send("order-completed", "Order completed: " + orderId);

    }

    public List<OrderDto> getOrdersByCustomerId(Integer customerId) {
        if(customerId == null){
            throw new InvalidOrderDataException("Order id cannot be null");
        }

        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if(orders.isEmpty()){
            throw new NoOrderFoundException("No order found for the given customer ID");
        }

        return orders.stream()
                .map(orderMapper::fromOrderToDto)
                .toList();
    }
}
