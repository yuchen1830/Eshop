package com.eshop.dto;

import com.eshop.enums.CategoryEnums;
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
public class ProductDto {
    private UUID id;
    private String productName;
    private String description;
    private BigDecimal price;
    private CategoryEnums category;
    private Integer stock;
}
