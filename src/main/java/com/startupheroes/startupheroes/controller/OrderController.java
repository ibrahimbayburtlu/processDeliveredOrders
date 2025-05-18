package com.startupheroes.startupheroes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.startupheroes.startupheroes.constant.endpoints;
import com.startupheroes.startupheroes.entity.Order;
import com.startupheroes.startupheroes.model.DeliveredOrder;
import com.startupheroes.startupheroes.service.OrderService;
import com.startupheroes.startupheroes.exception.OrderNotFoundException;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(endpoints.ORDERS)
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping(endpoints.ORDERS_ID)
    public Order getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            throw new OrderNotFoundException(id);
        }
        return order;
    }

    @GetMapping(endpoints.ORDERS_PROCESS)
    public List<DeliveredOrder> processDeliveredOrders(@PathVariable LocalDate date) {
        return orderService.processDeliveredOrders(date);
    }
}
