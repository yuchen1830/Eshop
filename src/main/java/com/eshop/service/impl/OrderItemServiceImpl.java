package com.eshop.service.impl;

import com.eshop.dto.OrderItemDto;
import com.eshop.entity.Order;
import com.eshop.entity.OrderItem;
import com.eshop.entity.Product;
import com.eshop.exception.ResourceNotFoundException;
import com.eshop.mapper.OrderItemMapper;
import com.eshop.repository.OrderItemRepository;
import com.eshop.repository.OrderRepository;
import com.eshop.repository.ProductRepository;
import com.eshop.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
//    private Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);
    private OrderItemRepository orderItemRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    @Override
    public OrderItemDto addOrderItem(Long orderId, UUID productId, Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));


        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        int remainStock = product.getStock() - quantity;
        if (remainStock < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        // Create and save the order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getPrice());
        orderItem.calculateTotalPrice();

        // Update product stock
        product.setStock(remainStock);
        productRepository.save(product);

        order.addOrderItem(orderItem);
        order.calculateTotalAmount();  // Update total amount based on new order item
        orderRepository.save(order);   // Save the updated order

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        return OrderItemMapper.mapToOrderItemDto(savedOrderItem);
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return orderItems.stream().map(OrderItemMapper::mapToOrderItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + orderItemId + " is not found"));

        orderItemRepository.deleteById(orderItemId);
    }

    @Override
    public void deleteAllItemsInOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        orderItemRepository.deleteByOrder(order);
    }
}
