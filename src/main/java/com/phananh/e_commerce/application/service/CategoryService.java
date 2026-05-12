package com.phananh.e_commerce.application.service;

import com.phananh.e_commerce.presentation.dto.request.category.CategoryCreateRequest;
import com.phananh.e_commerce.presentation.dto.request.category.CategorySearchRequest;
import com.phananh.e_commerce.presentation.dto.request.category.CategoryUpdateRequest;
import com.phananh.e_commerce.presentation.dto.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories(CategorySearchRequest categorySearchRequest);

    CategoryResponse createCategory(CategoryCreateRequest request);

    CategoryResponse updateCategory(CategoryUpdateRequest request);
}

