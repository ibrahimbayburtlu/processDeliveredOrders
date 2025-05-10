package com.startupheroes.startupheroes.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.startupheroes.startupheroes.entity.Order;
import com.startupheroes.startupheroes.model.DeliveredOrder;
import com.startupheroes.startupheroes.repository.OrderRepository;
import com.startupheroes.startupheroes.service.OrderService;
import com.startupheroes.startupheroes.kafka.OrderKafkaProducer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final OrderKafkaProducer kafkaProducer;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Delivery date is the date when the order will be delivered
    public List<DeliveredOrder> processDeliveredOrders(LocalDate date) {
        List<Order> orders = orderRepository.findByDeliveryAt(date);

        List<DeliveredOrder> deliveredOrders = orders.stream()
            .map(order -> {
                DeliveredOrder deliveredOrder = DeliveredOrder.builder()
                    .id(order.getId())
                    .created_at(order.getCreatedAt().format(DATE_FORMATTER))
                    .last_updated_at(order.getLastUpdatedAt().format(DATE_FORMATTER))
                    .build();
                
                // Collection duration
                if (order.getCollectionStartedAt() != null && order.getCollectedAt() !=null) {
                    deliveredOrder.setCollection_duration(
                        (int) java.time.Duration.between(order.getCollectionStartedAt(), order.getCollectedAt()).toMinutes()
                    );
                }
                
                // Delivery duration
                if (order.getDeliveryStartedAt() != null && order.getDeliveryAt() != null) {
                    deliveredOrder.setDelivery_duration(
                        (int) java.time.Duration.between(order.getDeliveryStartedAt(), order.getDeliveryAt()).toMinutes()
                    );
                }
                
                // ETA (immutable)
                deliveredOrder.setEta(order.getEta());
                
                // Total time from order creation to delivery
                deliveredOrder.setLead_time(
                    (int) java.time.Duration.between(order.getCreatedAt(),order.getDeliveryAt()).toMinutes()
                );
                
                // Order in time control LeadTime : actual delivery minutes, Eta : expected delivery minutes
                deliveredOrder.setOrder_in_time(deliveredOrder.getLead_time() <= order.getEta());
                
                return deliveredOrder;
            })
            .collect(Collectors.toList());
        
        // Send each DeliveredOrder to Kafka
        deliveredOrders.forEach(kafkaProducer::sendDeliveredOrder);
        
        return deliveredOrders;
    }
}
