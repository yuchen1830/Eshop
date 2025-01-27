package com.eshop.service.impl;

import com.eshop.dto.OrderDto;
import com.eshop.dto.OrderItemDto;
import com.eshop.entity.Order;
import com.eshop.entity.OrderItem;
import com.eshop.entity.Product;
import com.eshop.entity.User;
import com.eshop.exception.ResourceNotFoundException;
import com.eshop.mapper.OrderMapper;
import com.eshop.repository.OrderRepository;
import com.eshop.repository.UserRepository;
import com.eshop.service.OrderItemService;
import com.eshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
