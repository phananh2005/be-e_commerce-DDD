package com.phananh.e_commerce.productcatalog.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.*;
import com.phananh.e_commerce.productcatalog.application.dto.response.CategoryResponse;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Danh mục", description = "Các API quản lý danh mục sản phẩm")
public class CategoryController {

    CategoryService categoryService;

    // customer
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getEnabledCategories() {
        List<CategoryResponse> categories = categoryService.getEnabledCategories();
        ApiResponse<List<CategoryResponse>> apiResponse = ApiResponse.<List<CategoryResponse>>builder()
                .message("Categories retrieved successfully")
                .result(categories)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // admin
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllCategoriesBySearch(@Valid @ModelAttribute CategorySearchRequest categorySearchRequest) {
        Page<CategoryResponse> categories = categoryService.getAllCategoriesBySearch(categorySearchRequest);
        ApiResponse<Page<CategoryResponse>> apiResponse = ApiResponse.<Page<CategoryResponse>>builder()
                .message("Categories retrieved successfully")
                .result(categories)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        categoryService.createCategory(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryUpdateRequest request) {
        categoryService.updateCategory(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{categoryId}/{status}")
    public ResponseEntity<?> updateCategoryStatus(@PathVariable Long categoryId, @PathVariable String status) {
        categoryService.updateCategoryStatus(categoryId, status);
        return ResponseEntity.noContent().build();
    }
}


