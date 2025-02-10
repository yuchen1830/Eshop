package com.eshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "total_price")
    private BigDecimal totalPrice = BigDecimal.ZERO;

//    void setQuantity(Integer quantity) {
//        this.quantity = quantity;
//        calculateTotalPrice();
//    }

    public void calculateTotalPrice() {
        this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }


}
