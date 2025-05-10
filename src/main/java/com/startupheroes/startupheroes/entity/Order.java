package com.startupheroes.startupheroes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    // Order ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Order created at
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Order last updated at
    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    // Order collection started at
    @Column(name = "collection_started_at")
    private LocalDateTime collectionStartedAt;

    // Order collected at
    @Column(name = "collected_at")
    private LocalDateTime collectedAt;

    // Order delivery started at
    @Column(name = "delivery_started_at")
    private LocalDateTime deliveryStartedAt;

    // Order delivered at
    @Column(name = "delivery_at")
    private LocalDateTime deliveryAt;

    // Order ETA
    @Column(name = "eta", nullable = false)
    private int eta;

    // Order customer ID
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
}
