package com.aya.order_service.repository;

import com.aya.order_service.entity.Order;
import com.aya.order_service.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerId(Integer customerId);

    List<Order> findByStatus(OrderStatus status);
}
