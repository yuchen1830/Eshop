package com.eshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "orderStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "address")
    private String address;

    public enum OrderStatus {
        PENDING, PROCESS, SHIPPED, DELIVERED, CANCELLED
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }


    // Calculate total amount based on order items
    public void calculateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
