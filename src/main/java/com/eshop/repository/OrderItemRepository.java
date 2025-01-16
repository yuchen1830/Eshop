package com.eshop.repository;

import com.eshop.entity.Order;
import com.eshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
    void deleteByOrder(Order order);
}
