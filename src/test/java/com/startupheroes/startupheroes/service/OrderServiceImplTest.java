package com.startupheroes.startupheroes.service;

import com.startupheroes.startupheroes.entity.Order;
import com.startupheroes.startupheroes.model.DeliveredOrder;
import com.startupheroes.startupheroes.repository.OrderRepository;
import com.startupheroes.startupheroes.service.impl.OrderServiceImpl;
import com.startupheroes.startupheroes.kafka.OrderKafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Order Service Tests")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderKafkaProducer kafkaProducer;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order sampleOrder;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        now = LocalDateTime.now();
        sampleOrder = createSampleOrder();
    }

    private Order createSampleOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setCreatedAt(now.minusHours(1));
        order.setLastUpdatedAt(now);
        order.setCollectionStartedAt(now.minusMinutes(50));
        order.setCollectedAt(now.minusMinutes(40));
        order.setDeliveryStartedAt(now.minusMinutes(30));
        order.setDeliveryAt(now.minusMinutes(20));
        order.setEta(45); // 45 minutes ETA
        return order;
    }

    @Nested
    @DisplayName("Order Retrieval Tests")
    class OrderRetrievalTests {

        @Test
        @DisplayName("Should return all orders when getAllOrders is called")
        void getAllOrders_ShouldReturnAllOrders() {
            // Arrange
            List<Order> expectedOrders = Arrays.asList(sampleOrder);
            when(orderRepository.findAll()).thenReturn(expectedOrders);

            // Act
            List<Order> actualOrders = orderService.getAllOrders();

            // Assert
            assertNotNull(actualOrders);
            assertEquals(expectedOrders.size(), actualOrders.size());
            verify(orderRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return order when getOrderById is called with existing ID")
        void getOrderById_WhenOrderExists_ShouldReturnOrder() {
            // Arrange
            when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

            // Act
            Order actualOrder = orderService.getOrderById(1L);

            // Assert
            assertNotNull(actualOrder);
            assertEquals(sampleOrder.getId(), actualOrder.getId());
            verify(orderRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should return null when getOrderById is called with non-existing ID")
        void getOrderById_WhenOrderDoesNotExist_ShouldReturnNull() {
            // Arrange
            when(orderRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Order actualOrder = orderService.getOrderById(999L);

            // Assert
            assertNull(actualOrder);
            verify(orderRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Order Processing Tests")
    class OrderProcessingTests {

        @Test
        @DisplayName("Should process delivered orders and send to Kafka")
        void processDeliveredOrders_ShouldProcessAndSendToKafka() {
            // Arrange
            LocalDateTime deliveryDate = now.toLocalDate().atStartOfDay();
            List<Order> orders = Arrays.asList(sampleOrder);
            when(orderRepository.findByDeliveryAt(deliveryDate.toLocalDate())).thenReturn(orders);

            // Act
            List<DeliveredOrder> deliveredOrders = orderService.processDeliveredOrders(deliveryDate.toLocalDate());

            // Assert
            assertNotNull(deliveredOrders);
            assertEquals(1, deliveredOrders.size());
            
            DeliveredOrder deliveredOrder = deliveredOrders.get(0);
            assertEquals(sampleOrder.getId(), deliveredOrder.getId());
            assertEquals(10, deliveredOrder.getCollection_duration()); // 50 - 40 = 10 minutes
            assertEquals(10, deliveredOrder.getDelivery_duration()); // 30 - 20 = 10 minutes
            assertEquals(40, deliveredOrder.getLead_time()); // 60 - 20 = 40 minutes
            assertTrue(deliveredOrder.getOrder_in_time()); // 40 < 45 ETA
            
            verify(kafkaProducer, times(1)).sendDeliveredOrder(any(DeliveredOrder.class));
        }

        @Test
        @DisplayName("Should handle orders with same-day delivery times")
        void processDeliveredOrders_WithSameDayDelivery_ShouldProcessCorrectly() {
            // Arrange
            Order sameDayOrder = new Order();
            sameDayOrder.setId(2L);
            sameDayOrder.setCreatedAt(now);
            sameDayOrder.setLastUpdatedAt(now);
            sameDayOrder.setCollectionStartedAt(now.minusMinutes(20));
            sameDayOrder.setCollectedAt(now.minusMinutes(10));
            sameDayOrder.setDeliveryStartedAt(now.minusMinutes(5));
            sameDayOrder.setDeliveryAt(now);
            sameDayOrder.setEta(30);
            
            List<Order> orders = Arrays.asList(sameDayOrder);
            when(orderRepository.findByDeliveryAt(now.toLocalDate())).thenReturn(orders);

            // Act
            List<DeliveredOrder> deliveredOrders = orderService.processDeliveredOrders(now.toLocalDate());

            // Assert
            assertNotNull(deliveredOrders);
            assertEquals(1, deliveredOrders.size());
            
            DeliveredOrder deliveredOrder = deliveredOrders.get(0);
            assertEquals(sameDayOrder.getId(), deliveredOrder.getId());
            assertEquals(10, deliveredOrder.getCollection_duration()); // 20 - 10 = 10 minutes
            assertEquals(5, deliveredOrder.getDelivery_duration()); // 5 - 0 = 5 minutes
            assertEquals(0, deliveredOrder.getLead_time()); // now - now = 0 minutes
            assertTrue(deliveredOrder.getOrder_in_time()); // 0 < 30 ETA
            
            verify(kafkaProducer, times(1)).sendDeliveredOrder(any(DeliveredOrder.class));
        }
    }
} 