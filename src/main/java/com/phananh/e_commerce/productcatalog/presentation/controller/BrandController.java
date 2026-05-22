package com.phananh.e_commerce.productcatalog.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.*;
import com.phananh.e_commerce.productcatalog.application.dto.response.BrandResponse;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
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
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Thương hiệu", description = "Các API quản lý thương hiệu sản phẩm")
public class BrandController {

    BrandService brandService;

    //customer
    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getBrandsActive() {
        List<BrandResponse> brands = brandService.getBrandActive();
        ApiResponse<List<BrandResponse>> response = ApiResponse.<List<BrandResponse>>builder()
                .message("Get brands successfully")
                .result(brands)
                .build();
        return ResponseEntity.ok(response);
    }

    //admin
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BrandResponse>>> getBrandsBySearch(@Valid @ModelAttribute BrandSearchRequest request) {
        Page<BrandResponse> brands = brandService.getBrandsBySearch(request);
        ApiResponse<Page<BrandResponse>> response = ApiResponse.<Page<BrandResponse>>builder()
                .message("Get brands successfully")
                .result(brands)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBrand(@Valid @ModelAttribute BrandCreateRequest request) {
        brandService.createBrand(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBrand(@Valid @ModelAttribute BrandUpdateRequest request) {
        brandService.updateBrand(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{brandId}/{status}")
    public ResponseEntity<?> updateBrandStatus(@PathVariable Long brandId, @PathVariable String status) {
        brandService.updateBrandStatus(brandId, status);
        return ResponseEntity.noContent().build();
    }
}


