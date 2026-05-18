package com.phananh.e_commerce.product.persistence.repository.springdata;

import com.phananh.e_commerce.productcatalog.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpringDataCategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Page<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword1, String keyword2, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
    List<Category> findByIsEnabledTrue();
}


