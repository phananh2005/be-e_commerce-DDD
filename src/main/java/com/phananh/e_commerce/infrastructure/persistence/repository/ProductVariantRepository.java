package com.phananh.e_commerce.infrastructure.persistence.repository;

import com.phananh.e_commerce.domain.model.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<List<ProductVariant>> findByProduct_Id(Long productId);
}
