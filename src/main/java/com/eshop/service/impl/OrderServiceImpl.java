package com.eshop.service.impl;

import com.eshop.dto.OrderDto;
import com.eshop.dto.OrderItemDto;
import com.eshop.dto.ProductDto;
import com.eshop.entity.*;
import com.eshop.exception.BusinessException;
import com.eshop.exception.ResourceNotFoundException;
import com.eshop.mapper.OrderMapper;
import com.eshop.repository.OrderRepository;
import com.eshop.repository.ProductRepository;
import com.eshop.repository.UserRepository;
import com.eshop.service.OrderItemService;
import com.eshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private OrderItemService orderItemService;
    private ProductRepository productRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUserDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setAddress(orderDto.getAddress());
        Order preSavedOrder = orderRepository.save(order);

        for(OrderItemDto item : orderDto.getOrderItems()) {
            orderItemService.addOrderItem(order.getId(), item.getProductId(), item.getQuantity());
        }

        preSavedOrder.calculateTotalAmount();
        Order savedOrder = orderRepository.save(preSavedOrder);
        return OrderMapper.mapToOrderDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto submitOrder(User user, List<CartItem> cartItems, String address) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setAddress(address);
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item : cartItems) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                try {
                    throw new BusinessException("Insufficient stock");
                } catch (BusinessException e) {
                    throw new RuntimeException(e);
                }
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProduct(product);
            orderItem.setUnitPrice(item.getUnitPrice());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.calculateTotalAmount();
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapToOrderDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + orderId + " is not found"));
        return OrderMapper.mapToOrderDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders= orderRepository.findAll();
        return orders.stream().map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrder(Long orderId, OrderDto updateOrder) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order " + orderId + " is not found")
        );
        order.setOrderStatus(updateOrder.getOrderStatus());
        order.setAddress(updateOrder.getAddress());
        order.setTotalAmount(updateOrder.getTotalAmount());
        Order updateOrderDto = orderRepository.save(order);
        return OrderMapper.mapToOrderDto(updateOrderDto);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + orderId + " is not found"));

        // cascades will handle the items deletion
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderDto> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersWithinDateRange(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        return orders.stream().map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    //TODO: update order status depends on shipping service


}
