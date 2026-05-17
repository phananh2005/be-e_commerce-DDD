package com.phananh.e_commerce.productcatalog.domain.repository;

import com.phananh.e_commerce.productcatalog.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository {
    Page<Category> getAll(Pageable pageable);
    Page<Category> getAllBySearch(String keyword, Pageable pageable);
}
