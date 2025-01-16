package com.eshop.mapper;

import com.eshop.dto.OrderItemDto;
import com.eshop.entity.Order;
import com.eshop.entity.OrderItem;
import com.eshop.entity.Product;
import com.eshop.repository.OrderRepository;
import com.eshop.repository.ProductRepository;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OrderItemMapper {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public static OrderItemDto mapToOrderItemDto(OrderItem orderitem){
        return new OrderItemDto(
                orderitem.getId(),
                orderitem.getOrder().getId(),
                orderitem.getProduct().getId(),
                orderitem.getProduct().getProductName(),
                orderitem.getQuantity(),
                orderitem.getUnitPrice(),
                orderitem.getTotalPrice()
        );
    }

    public static OrderItem mapToOrderItem(OrderItemDto orderitemDto, Product product, Order order){
        return new OrderItem(
                orderitemDto.getId(),
                product,
                order,
                orderitemDto.getQuantity(),
                orderitemDto.getUnitPrice(),
                orderitemDto.getTotalPrice()
        );
    }

}
