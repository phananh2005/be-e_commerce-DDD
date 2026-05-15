package com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.productcatalog.domain.model.Category;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataCategoryRepository extends JpaRepository<Category, Long> {
    Optional<List<Category>> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword1, String keyword2, PageRequest pageRequest);
    boolean existsByNameIgnoreCase(String name);
}


