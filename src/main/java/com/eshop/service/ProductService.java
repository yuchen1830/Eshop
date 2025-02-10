package com.eshop.service;

import com.eshop.dto.ProductDto;
import com.eshop.dto.UserDto;
import com.eshop.enums.CategoryEnums;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(UUID productId);

    List<ProductDto> getAllProducts();

    ProductDto updateProduct(UUID productId, ProductDto updateProduct);
    void deleteProduct(UUID productId);

    List<ProductDto> getProductByCategory(CategoryEnums category);
}
