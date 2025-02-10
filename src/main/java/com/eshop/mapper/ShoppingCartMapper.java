package com.eshop.mapper;

import com.eshop.dto.ShoppingCartDto;
import com.eshop.entity.ShoppingCart;

import java.util.stream.Collectors;

public class ShoppingCartMapper {
    public static ShoppingCartDto mapToShoppingCartDto(ShoppingCart shoppingCart) {
        return new ShoppingCartDto(
                shoppingCart.getUser().getId(),
                shoppingCart.getCartItems().stream().map(CartItemMapper::mapToCartItemDto)
                        .collect(Collectors.toList()),
                shoppingCart.getTotalAmount()
                );
    }
}
