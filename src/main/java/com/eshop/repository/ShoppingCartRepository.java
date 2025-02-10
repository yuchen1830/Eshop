package com.eshop.repository;

import com.eshop.entity.CartItem;
import com.eshop.entity.Order;
import com.eshop.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByUserId(Long userId);



}
