package com.eshop.service.impl;

import com.eshop.dto.ProductDto;
import com.eshop.entity.Product;
import com.eshop.entity.User;
import com.eshop.exception.ResourceNotFoundException;
import com.eshop.mapper.ProductMapper;
import com.eshop.mapper.UserMapper;
import com.eshop.repository.ProductRepository;
import com.eshop.service.ProductService;
import com.eshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product " + productId + " is not found"));
        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products= productRepository.findAll();
        return products.stream().map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto updateProduct(UUID productId, ProductDto updateProduct) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("User " + productId + " is not found\"")
        );
        product.setProductName(updateProduct.getProductName());
        product.setId(updateProduct.getId());
        product.setCategory(updateProduct.getCategory());
        product.setStock(updateProduct.getStock());
        product.setPrice(updateProduct.getPrice());
        product.setDescription(updateProduct.getDescription());
        Product updateProductDto = productRepository.save(product);
        return ProductMapper.mapToProductDto(updateProductDto);
    }

    @Override
    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + productId + " is not found"));

        productRepository.deleteById(productId);
    }
}
