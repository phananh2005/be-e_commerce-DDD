package com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.impl;

import com.phananh.e_commerce.productcatalog.domain.model.Category;
import com.phananh.e_commerce.productcatalog.domain.repository.CategoryRepository;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata.SpringDataCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CategoryRepositoryImpl implements CategoryRepository {

    SpringDataCategoryRepository springDataCategoryRepository;

    @Override
    public Page<Category> getAll(Pageable pageable) {
        return springDataCategoryRepository.findAll(pageable);
    }

    @Override
    public Page<Category> getAllBySearch(String keyword, Pageable pageable) {
        return springDataCategoryRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
    }
}
