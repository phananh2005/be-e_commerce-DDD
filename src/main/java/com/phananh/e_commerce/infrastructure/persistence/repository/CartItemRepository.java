package com.phananh.e_commerce.infrastructure.persistence.repository;

import com.phananh.e_commerce.domain.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<List<CartItem>> findByUser_Username(String username);
    void deleteByIdIn(List<Long> ids);
    Optional<CartItem> findByVariant_IdAndUser_Username(Long variantId, String userName);
}
