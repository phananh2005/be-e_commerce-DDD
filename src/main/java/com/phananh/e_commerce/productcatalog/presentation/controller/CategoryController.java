package com.phananh.e_commerce.productcatalog.presentation.controller;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategorySearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.category.CategoryResponse;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(@Valid @ModelAttribute CategorySearchRequest categorySearchRequest) {
        List<CategoryResponse> categories = categoryService.getAllCategories(categorySearchRequest);
        return ResponseEntity.ok(ApiResponse.<List<CategoryResponse>>builder()
                .message("Categories retrieved successfully")
                .result(categories)
                .build());
    }

    // admin
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @ModelAttribute CategoryCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .message("Category created successfully")
                .result(categoryService.createCategory(request))
                .build());
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@Valid @ModelAttribute CategoryUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .message("Category updated successfully")
                .result(categoryService.updateCategory(request))
                .build());
    }
}


