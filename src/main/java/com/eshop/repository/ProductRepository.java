package com.eshop.repository;

import com.eshop.entity.Product;
import com.eshop.entity.User;
import com.eshop.enums.CategoryEnums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategory(CategoryEnums category);

}
