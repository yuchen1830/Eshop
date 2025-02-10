package com.eshop.service;

import com.eshop.dto.OrderDto;
import com.eshop.dto.ShoppingCartDto;
import com.eshop.entity.Order;

import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getUserCart(Long userId);
    void addProductToCart(Long userId, UUID productId);
    void removeProductFromCart(Long userId, Long cartItemId);
    void updateQuantity(Long userId, Long cartItemId, int quantity);
    OrderDto checkout(Long userId, String address);
}
