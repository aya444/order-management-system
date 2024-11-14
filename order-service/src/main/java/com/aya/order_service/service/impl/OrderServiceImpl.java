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
import com.aya.order_service.kafka.KafkaProducer;
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
    private final KafkaProducer kafkaProducer;
    private static final String ORDER_NOT_FOUND_MESSAGE = "Order not found!";
    private static final String ORDER_DATA_CANNOT_BE_NULL_MESSAGE = "Order data cannot be null!";
    private static final String ORDER_ID_CANNOT_BE_NULL_MESSAGE = "Order id cannot be null!";
    private static final String NO_PRODUCT_FOUND_MESSAGE = "No product found for the given product IDs";


    private Order getOrderById(Integer id) {
        if(id == null){
            throw new InvalidOrderDataException(ORDER_ID_CANNOT_BE_NULL_MESSAGE);
        }

        return orderRepository.findById(id)
                .orElseThrow(() -> new NoOrderFoundException(ORDER_NOT_FOUND_MESSAGE));
    }

    public OrderDto createOrder(OrderInputDto orderInputDto) {
        if(orderInputDto == null){
            throw new InvalidOrderDataException(ORDER_DATA_CANNOT_BE_NULL_MESSAGE);
        }

        // Convert ProductInputDto to ProductDto
        List<ProductInputDto> productInputDtos = orderInputDto.getProductInputDtoIds().stream()
                .map(productId -> new ProductInputDto(productId.getId(), productId.getQuantity()))
                .toList();

        if (productInputDtos.isEmpty()) {
            throw new NoProductFoundException(NO_PRODUCT_FOUND_MESSAGE);
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

        Order order = getOrderById(id);
        order.setStatus(OrderStatus.COMPLETED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        OrderDto orderDto = orderMapper.fromOrderToDto(order);

        // Send message to Kafka
        kafkaProducer.sendMessage(orderDto);
    }

    public void cancelOrder(Integer id) {

        Order order = getOrderById(id);
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    public List<OrderDto> getOrdersByCustomerId(Integer customerId) {
        if(customerId == null){
            throw new InvalidOrderDataException(ORDER_ID_CANNOT_BE_NULL_MESSAGE);
        }

        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if(orders.isEmpty()){
            throw new NoOrderFoundException(ORDER_NOT_FOUND_MESSAGE);
        }
        return orders.stream()
                .map(orderMapper::fromOrderToDto)
                .toList();
    }
}
