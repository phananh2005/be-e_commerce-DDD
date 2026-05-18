package com.phananh.e_commerce.productcatalog.application.service;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryImageUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategorySearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryInfoUpdateRequest;
import com.phananh.e_commerce.productcatalog.application.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getEnabledCategories();
    Page<CategoryResponse> getAllCategoriesBySearch(CategorySearchRequest categorySearchRequest);

    void createCategory(CategoryCreateRequest request);

    void updateCategoryInfo(CategoryInfoUpdateRequest request);
    void updateCategoryImage(CategoryImageUpdateRequest request);
    void updateCategoryStatus(com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryStatusUpdateRequest request);
}


