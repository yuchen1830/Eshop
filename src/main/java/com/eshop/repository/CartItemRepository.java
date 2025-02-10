package com.eshop.repository;

import com.eshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
}
