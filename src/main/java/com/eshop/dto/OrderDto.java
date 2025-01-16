package com.eshop.dto;

import com.eshop.entity.Order;
import com.eshop.entity.OrderItem;
import com.eshop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private UserDto userDto;
    private Date orderDate;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalAmount;
    private Order.OrderStatus orderStatus;
    private String address;
}
