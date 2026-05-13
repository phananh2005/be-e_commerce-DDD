package com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository;

import com.phananh.e_commerce.productcatalog.domain.model.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
}


