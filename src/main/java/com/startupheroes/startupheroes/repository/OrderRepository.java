package com.startupheroes.startupheroes.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.startupheroes.startupheroes.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.deliveryAt) = :date")
    List<Order> findByDeliveryAt(@Param("date") LocalDate date);
}
