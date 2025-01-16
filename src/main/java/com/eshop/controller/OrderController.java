package com.eshop.controller;

import com.eshop.dto.OrderDto;
import com.eshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;

    /**
     * Add an Order
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){
        OrderDto savedOrder = orderService.createOrder(orderDto);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    /**
     * Get an Order
     */
    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("id") Long orderId){
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    /**
     * Get All Orders
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Update Order
     */
    @PutMapping("{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long orderId,
                                              @RequestBody OrderDto updatedOrder) {
        OrderDto orderDto = orderService.updateOrder(orderId, updatedOrder);
        return ResponseEntity.ok(orderDto);
    }

    /**
     * Delete Order
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order is deleted");
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<OrderDto>> getUserOrder(@RequestParam("userId") Long userId) {
        List<OrderDto> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("date-range")
    public ResponseEntity<List<OrderDto>> getOrderWithinDateRange (@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")Date endDate) {
        List<OrderDto> orders = orderService.getOrdersWithinDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }
}
