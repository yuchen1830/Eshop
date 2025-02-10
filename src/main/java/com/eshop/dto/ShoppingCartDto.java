package com.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {
    private Long userId;
    private List<CartItemDto> cartItems;
    private BigDecimal totalAmount;
}
