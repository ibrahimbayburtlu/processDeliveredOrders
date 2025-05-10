package com.startupheroes.startupheroes.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.startupheroes.startupheroes.model.DeliveredOrder;
import com.startupheroes.startupheroes.constant.KafkaTopics;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderKafkaProducer {
    
    private final KafkaTemplate<String, DeliveredOrder> kafkaTemplate;

    public void sendDeliveredOrder(DeliveredOrder deliveredOrder) {
        kafkaTemplate.send(KafkaTopics.ORDER_DELIVERY_STATISTICS, deliveredOrder);
    }
} 