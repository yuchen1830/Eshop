package com.eshop.controller;

import com.eshop.dto.OrderDto;
import com.eshop.dto.ShoppingCartDto;
import com.eshop.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class ShoppingCartController {
    private ShoppingCartService shoppingCartService;
    @GetMapping("/{userId}")
    public ResponseEntity<ShoppingCartDto> getUserCart(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingCartService.getUserCart(userId));
    }

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Void> addProductToCart(@PathVariable Long userId,
                                                 @PathVariable UUID productId) {
        shoppingCartService.addProductToCart(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/remove/{cartItemId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long userId,
                                                      @PathVariable Long cartItemId) {
        shoppingCartService.removeProductFromCart(userId, cartItemId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/update/{cartItemId}/{quantity}")
    public ResponseEntity<Void> updateQuantity(@PathVariable Long userId,
                                               @PathVariable Long cartItemId,
                                               @PathVariable int quantity){
        shoppingCartService.updateQuantity(userId, cartItemId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<OrderDto> checkout(@PathVariable Long userId, @RequestParam String address) {
        OrderDto orderDto = shoppingCartService.checkout(userId, address);
        return ResponseEntity.ok(orderDto);
    }

}
