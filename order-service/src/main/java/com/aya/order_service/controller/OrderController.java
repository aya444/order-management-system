package com.aya.order_service.controller;

import com.aya.order_service.dto.OrderDto;
import com.aya.order_service.dto.OrderInputDto;
import com.aya.order_service.service.impl.OrderServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final OrderServiceImpl orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody @NotNull OrderInputDto request) {
        OrderDto orderDto = orderService.createOrder(request);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<String> completeOrder(@PathVariable("id") @NotNull Integer id) {
        orderService.completeOrder(id);
        return new ResponseEntity<>("Status has been updated to complete", HttpStatus.OK);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable("id") @NotNull Integer id) {
        orderService.cancelOrder(id);
        return new ResponseEntity<>("Status has been updated to cancelled", HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable("id") @NotNull Integer id) {
        List<OrderDto> orders = orderService.getOrdersByCustomerId(id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
