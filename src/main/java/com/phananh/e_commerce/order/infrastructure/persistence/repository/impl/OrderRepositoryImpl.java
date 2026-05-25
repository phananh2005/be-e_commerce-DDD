package com.phananh.e_commerce.order.infrastructure.persistence.repository.impl;

import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.repository.OrderRepository;
import com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata.SpringDataOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderRepositoryImpl implements OrderRepository {

    SpringDataOrderRepository springDataOrderRepository;

    @Override
    public Order save(Order order) {
        return springDataOrderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return springDataOrderRepository.findById(id);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return springDataOrderRepository.findByUserId(userId);
    }
}


