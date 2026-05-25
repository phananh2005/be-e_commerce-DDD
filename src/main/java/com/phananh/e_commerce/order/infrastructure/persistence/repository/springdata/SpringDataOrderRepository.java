package com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}


