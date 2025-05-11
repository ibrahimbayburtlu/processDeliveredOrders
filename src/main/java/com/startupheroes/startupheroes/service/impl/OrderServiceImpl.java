package com.startupheroes.startupheroes.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.startupheroes.startupheroes.entity.Order;
import com.startupheroes.startupheroes.model.DeliveredOrder;
import com.startupheroes.startupheroes.repository.OrderRepository;
import com.startupheroes.startupheroes.service.OrderService;
import com.startupheroes.startupheroes.kafka.OrderKafkaProducer;
import com.startupheroes.startupheroes.exception.OrderProcessingException;
import com.startupheroes.startupheroes.exception.KafkaMessageException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final OrderKafkaProducer kafkaProducer;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            logger.error("Error while fetching all orders", e);
            throw new OrderProcessingException("Failed to fetch orders", e);
        }
    }

    public Order getOrderById(Long id) {
        try {
            return orderRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error while fetching order with id: {}", id, e);
            throw new OrderProcessingException("Failed to fetch order with id: " + id, e);
        }
    }

    // Delivery date is the date when the order will be delivered
    public List<DeliveredOrder> processDeliveredOrders(LocalDate date) {
        try {
            List<Order> orders = orderRepository.findByDeliveryAt(date);
            logger.info("Found {} orders for date: {}", orders.size(), date);

            List<DeliveredOrder> deliveredOrders = orders.stream()
                .map(order -> {
                    try {
                        DeliveredOrder deliveredOrder = DeliveredOrder.builder()
                            .id(order.getId())
                            .created_at(order.getCreatedAt().format(DATE_FORMATTER))
                            .last_updated_at(order.getLastUpdatedAt().format(DATE_FORMATTER))
                            .build();
                        
                        // Collection duration
                        if (order.getCollectionStartedAt() != null && order.getCollectedAt() != null) {
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
                            (int) java.time.Duration.between(order.getCreatedAt(), order.getDeliveryAt()).toMinutes()
                        );
                        
                        // Order in time control LeadTime : actual delivery minutes, Eta : expected delivery minutes
                        deliveredOrder.setOrder_in_time(deliveredOrder.getLead_time() <= order.getEta());
                        
                        return deliveredOrder;
                    } catch (Exception e) {
                        logger.error("Error processing order with id: {}", order.getId(), e);
                        throw new OrderProcessingException("Failed to process order with id: " + order.getId(), e);
                    }
                })
                .collect(Collectors.toList());
            
            // Send each DeliveredOrder to Kafka
            deliveredOrders.forEach(deliveredOrder -> {
                try {
                    kafkaProducer.sendDeliveredOrder(deliveredOrder);
                    logger.info("Successfully sent order {} to Kafka", deliveredOrder.getId());
                } catch (Exception e) {
                    logger.error("Error sending order {} to Kafka", deliveredOrder.getId(), e);
                    throw new KafkaMessageException("Failed to send order to Kafka: " + deliveredOrder.getId(), e);
                }
            });
            
            return deliveredOrders;
        } catch (OrderProcessingException | KafkaMessageException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while processing delivered orders for date: {}", date, e);
            throw new OrderProcessingException("Failed to process delivered orders", e);
        }
    }
}
