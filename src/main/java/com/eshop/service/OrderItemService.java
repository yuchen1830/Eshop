package com.eshop.service;

import com.eshop.dto.OrderItemDto;
import com.eshop.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface OrderItemService {
    OrderItemDto addOrderItem(Long orderId, UUID productId, Integer quantity);
    List<OrderItemDto> getOrderItemsByOrderId(Long orderId);

    void deleteOrderItem(Long orderItemId);

    void deleteAllItemsInOrder(Long orderId);
}
