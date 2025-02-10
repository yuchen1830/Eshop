package com.eshop.service.impl;

import com.eshop.dto.OrderDto;
import com.eshop.dto.ShoppingCartDto;
import com.eshop.entity.CartItem;
import com.eshop.entity.Product;
import com.eshop.entity.ShoppingCart;
import com.eshop.entity.User;
import com.eshop.exception.ResourceNotFoundException;
import com.eshop.mapper.ShoppingCartMapper;
import com.eshop.repository.ProductRepository;
import com.eshop.repository.ShoppingCartRepository;
import com.eshop.repository.UserRepository;
import com.eshop.service.OrderService;
import com.eshop.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private OrderService orderService;


    @Override
    public ShoppingCartDto getUserCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);

        if(cart == null) {
            cart = new ShoppingCart();
            cart.setUser(user);
            cart.setCartItems(new ArrayList<>());
            cart.setTotalAmount(BigDecimal.ZERO);
            shoppingCartRepository.save(cart);
        }
        return ShoppingCartMapper.mapToShoppingCartDto(cart);
    }

    @Override
    public void addProductToCart(Long userId, UUID productId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        Product product = productRepository.findById(productId).orElseThrow(() ->
        new ResourceNotFoundException("Product not found"));
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item-> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
        if(existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            CartItem newItem = new CartItem();
            newItem.setShoppingCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(1);
            newItem.setUnitPrice(product.getPrice());
            newItem.setTotalPrice(product.getPrice());
            cart.getCartItems().add(newItem);
        }
        cart.calculateTotalAmount();
        shoppingCartRepository.save(cart);
    }

    @Override
    public void removeProductFromCart(Long userId, Long cartItemId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        CartItem targetItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));
        cart.getCartItems().remove(targetItem);
        cart.calculateTotalAmount();
        shoppingCartRepository.save(cart);

    }

    @Override
    public void updateQuantity(Long userId, Long cartItemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        CartItem targetItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));
        targetItem.setQuantity(targetItem.getQuantity() + quantity);
        targetItem.calculateTotalPrice();
        cart.calculateTotalAmount();
        shoppingCartRepository.save(cart);
    }

    @Override
    public OrderDto checkout(Long userId, String address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        if(cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Shopping cart is empty");
        }

        // create order
        OrderDto orderDto = orderService.submitOrder(user, cart.getCartItems(), address);

        // clear cart
        cart.getCartItems().clear();
        cart.calculateTotalAmount();
        shoppingCartRepository.save(cart);
        return orderDto;
    }
}
