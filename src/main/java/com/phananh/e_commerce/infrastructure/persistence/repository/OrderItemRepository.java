package com.phananh.e_commerce.infrastructure.persistence.repository;

import com.phananh.e_commerce.domain.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_Id(Long orderId);
}
