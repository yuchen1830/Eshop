package com.eshop.service;

import com.eshop.dto.OrderDto;
import com.eshop.entity.CartItem;
import com.eshop.entity.User;

import java.util.Date;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);

    OrderDto submitOrder(User user, List<CartItem> cartItems, String address);

    OrderDto getOrderById(Long orderId);

    List<OrderDto> getAllOrders();

    OrderDto updateOrder(Long orderId, OrderDto updateOrder);

    void deleteOrder(Long orderId);

    List<OrderDto> getOrdersByUser(Long userId);

    List<OrderDto> getOrdersWithinDateRange(Date startDate, Date endDate);
}
