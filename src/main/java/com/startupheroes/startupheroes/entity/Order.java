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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    @Column(name = "collection_started_at")
    private LocalDateTime collectionStartedAt;

    @Column(name = "collected_at")
    private LocalDateTime collectedAt;

    @Column(name = "delivery_started_at")
    private LocalDateTime deliveryStartedAt;

    @Column(name = "delivery_at")
    private LocalDateTime deliveryAt;

    @Column(name = "eta", nullable = false)
    private int eta;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;
}
