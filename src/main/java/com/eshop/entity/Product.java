package com.eshop.entity;

import com.eshop.enums.CategoryEnums;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;


    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private CategoryEnums category;
    //TODO: interact with frontend with String or Integer

    @Column(name = "stock", nullable = false)
    @Min(0)
    private Integer stock;
}
