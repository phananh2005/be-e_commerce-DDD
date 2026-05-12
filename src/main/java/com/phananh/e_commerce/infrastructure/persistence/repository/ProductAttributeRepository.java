package com.phananh.e_commerce.infrastructure.persistence.repository;

import com.phananh.e_commerce.domain.model.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
}
