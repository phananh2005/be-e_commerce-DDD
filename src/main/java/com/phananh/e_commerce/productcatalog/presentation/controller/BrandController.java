package com.phananh.e_commerce.productcatalog.presentation.controller;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandImageUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandInfoUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.brand.BrandResponse;
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

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Thương hiệu", description = "Các API quản lý thương hiệu sản phẩm")
public class BrandController {

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBrand(@Valid @ModelAttribute BrandCreateRequest request) {
        brandService.createBrand(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<?> updateBrand(@Valid @ModelAttribute BrandInfoUpdateRequest request) {
        brandService.updateBrandInfo(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBrandImage(@Valid @ModelAttribute BrandImageUpdateRequest request) {
        brandService.updateBrandImage(request);
        return ResponseEntity.noContent().build();
    }
}


