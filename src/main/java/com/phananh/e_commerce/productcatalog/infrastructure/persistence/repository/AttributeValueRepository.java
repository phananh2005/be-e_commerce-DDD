package com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository;

import com.phananh.e_commerce.productcatalog.domain.model.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
}


