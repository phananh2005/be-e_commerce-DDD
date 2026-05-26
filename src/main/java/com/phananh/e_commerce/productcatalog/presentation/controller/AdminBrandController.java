package com.phananh.e_commerce.productcatalog.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandUpdateRequest;
import com.phananh.e_commerce.productcatalog.application.dto.response.BrandResponse;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quản trị - Thương hiệu", description = "API quản trị thương hiệu sản phẩm")
public class AdminBrandController {

    BrandService brandService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BrandResponse>>> getBrandsBySearch(@Valid @ModelAttribute BrandSearchRequest request) {
        Page<BrandResponse> brands = brandService.getBrandsBySearch(request);
        ApiResponse<Page<BrandResponse>> response = ApiResponse.<Page<BrandResponse>>builder()
                .message("Get brands successfully")
                .result(brands)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody BrandCreateRequest request) {
        brandService.createBrand(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateBrand(@Valid @RequestBody BrandUpdateRequest request) {
        brandService.updateBrand(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{brandId}/{status}")
    public ResponseEntity<?> updateBrandStatus(@PathVariable Long brandId, @PathVariable String status) {
        brandService.updateBrandStatus(brandId, status);
        return ResponseEntity.noContent().build();
    }
}

