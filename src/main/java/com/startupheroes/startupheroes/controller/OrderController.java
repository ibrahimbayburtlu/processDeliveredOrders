package com.startupheroes.startupheroes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.startupheroes.startupheroes.entity.Order;
import com.startupheroes.startupheroes.model.DeliveredOrder;
import com.startupheroes.startupheroes.service.OrderService;
import com.startupheroes.startupheroes.exception.OrderNotFoundException;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            throw new OrderNotFoundException(id);
        }
        return order;
    }

    @GetMapping("/orders/process/{date}")
    public List<DeliveredOrder> processDeliveredOrders(@PathVariable LocalDate date) {
        return orderService.processDeliveredOrders(date);
    }
}
