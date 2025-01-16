package com.eshop.mapper;

import com.eshop.dto.OrderDto;
import com.eshop.entity.Order;
import com.eshop.entity.OrderItem;
import com.eshop.entity.Product;
import com.eshop.entity.User;
import com.eshop.repository.OrderRepository;
import com.eshop.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto mapToOrderDto(Order order) {
        return new OrderDto(
                order.getId(),
                UserMapper.mapToUserDto(order.getUser()),
                order.getOrderDate(),
                order.getOrderItems().stream()
                        .map(OrderItemMapper::mapToOrderItemDto)
                        .collect(Collectors.toList()),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getAddress()
        );
    }

    public static Order mapToOrder(OrderDto orderDto, List<OrderItem> orderItems) {
        if(orderDto == null) return null;
        return new Order(
                orderDto.getId(),
                UserMapper.mapToUser(orderDto.getUserDto()),
                orderDto.getOrderDate(),
                orderItems!= null? orderItems : new ArrayList<>(),
                orderDto.getTotalAmount(),
                orderDto.getOrderStatus(),
                orderDto.getAddress()
        );
    }

}
