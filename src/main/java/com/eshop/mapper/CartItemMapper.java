package com.eshop.mapper;

import com.eshop.dto.CartItemDto;
import com.eshop.dto.OrderItemDto;
import com.eshop.entity.*;

import java.math.BigDecimal;

public class CartItemMapper {
    public static CartItemDto mapToCartItemDto(CartItem cartitem){
        return new CartItemDto(
                cartitem.getId(),
                cartitem.getProduct().getId(),
                cartitem.getQuantity()
        );
    }

    public static CartItem mapToCartItem(CartItemDto cartItemDto, Product product, ShoppingCart shoppingCart){
        return new CartItem(
                cartItemDto.getCartItemId(),
                shoppingCart,
                product,
                cartItemDto.getQuantity(),
                product.getPrice(),
                product.getPrice().multiply(BigDecimal.valueOf(cartItemDto.getQuantity()))
        );
    }
}
