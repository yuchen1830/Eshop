package com.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long cartItemId;
    private UUID productId;
    private Integer quantity;
}
