package com.startupheroes.startupheroes.controller;

import com.startupheroes.startupheroes.entity.Order;
import com.startupheroes.startupheroes.model.DeliveredOrder;
import com.startupheroes.startupheroes.service.OrderService;
import com.startupheroes.startupheroes.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Order Controller Tests")
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private Order sampleOrder;
    private DeliveredOrder sampleDeliveredOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
        
        // Sample order setup
        sampleOrder = new Order();
        sampleOrder.setId(1L);
        
        // Sample delivered order setup
        sampleDeliveredOrder = DeliveredOrder.builder()
            .id(1L)
            .created_at("2024-02-20 10:00:00")
            .last_updated_at("2024-02-20 11:00:00")
            .collection_duration(10)
            .delivery_duration(15)
            .eta(45)
            .lead_time(40)
            .order_in_time(true)
            .build();
    }

    @Nested
    @DisplayName("Order Retrieval Endpoint Tests")
    class OrderRetrievalTests {

        @Test
        @DisplayName("GET /orders should return all orders")
        void getAllOrders_ShouldReturnAllOrders() throws Exception {
            // Arrange
            List<Order> orders = Arrays.asList(sampleOrder);
            when(orderService.getAllOrders()).thenReturn(orders);

            // Act & Assert
            mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
        }

        @Test
        @DisplayName("GET /orders/{id} should return order when exists")
        void getOrderById_WhenExists_ShouldReturnOrder() throws Exception {
            // Arrange
            when(orderService.getOrderById(1L)).thenReturn(sampleOrder);

            // Act & Assert
            mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
        }

        @Test
        @DisplayName("GET /orders/{id} should return 404 when order not found")
        void getOrderById_WhenNotExists_ShouldReturn404() throws Exception {
            // Arrange
            when(orderService.getOrderById(999L)).thenReturn(null);

            // Act & Assert
            mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Order not found with id: 999")));
        }
    }

    @Nested
    @DisplayName("Process Delivered Orders Endpoint Tests")
    class ProcessDeliveredOrdersTests {

        @Test
        @DisplayName("GET /orders/process/{date} should return processed orders")
        void processDeliveredOrders_ShouldReturnProcessedOrders() throws Exception {
            // Arrange
            List<DeliveredOrder> deliveredOrders = Arrays.asList(sampleDeliveredOrder);
            when(orderService.processDeliveredOrders(any(LocalDate.class))).thenReturn(deliveredOrders);

            // Act & Assert
            mockMvc.perform(get("/orders/process/2024-02-20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].order_in_time", is(true)))
                .andExpect(jsonPath("$[0].lead_time", is(40)));
        }

        @Test
        @DisplayName("GET /orders/process/{date} should return empty list when no orders")
        void processDeliveredOrders_WhenNoOrders_ShouldReturnEmptyList() throws Exception {
            // Arrange
            when(orderService.processDeliveredOrders(any(LocalDate.class))).thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get("/orders/process/2024-02-20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("GET /orders/process/{date} should return 400 when date is invalid")
        void processDeliveredOrders_WithInvalidDate_ShouldReturn400() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/orders/process/invalid-date"))
                .andExpect(status().isBadRequest());
        }
    }
}