package com.startupheroes.startupheroes.service;

import java.util.List;
import java.time.LocalDate;
import com.startupheroes.startupheroes.entity.Order;
import com.startupheroes.startupheroes.model.DeliveredOrder;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    List<DeliveredOrder> processDeliveredOrders(LocalDate date);
}