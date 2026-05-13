package com.phananh.e_commerce.order.infrastructure.persistence.repository;

import com.phananh.e_commerce.order.domain.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Username(String userName);
}


