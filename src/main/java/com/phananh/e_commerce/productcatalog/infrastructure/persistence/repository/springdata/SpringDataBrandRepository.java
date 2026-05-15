package com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBrandRepository extends JpaRepository<Brand, Long> {
    Page<Brand> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameKeyword, String descriptionKeyword, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}


