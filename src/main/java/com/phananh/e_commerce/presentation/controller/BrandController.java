package com.phananh.e_commerce.presentation.controller;

import com.phananh.e_commerce.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.presentation.dto.request.brand.BrandUpdateRequest;
import com.phananh.e_commerce.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.presentation.dto.response.brand.BrandResponse;
import com.phananh.e_commerce.application.service.BrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "ThÆ°Æ¡ng hiá»‡u", description = "CÃ¡c API quáº£n lÃ½ thÆ°Æ¡ng hiá»‡u sáº£n pháº©m")
public class BrandController {

    BrandService brandService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getBrandsBySearch(@Valid @ModelAttribute BrandSearchRequest request) {
        List<BrandResponse> brands = brandService.getBrandsBySearch(request);
        ApiResponse<List<BrandResponse>> response = ApiResponse.<List<BrandResponse>>builder()
                .message("Get brands successfully")
                .result(brands)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(@Valid @ModelAttribute BrandCreateRequest request) {
        BrandResponse brand = brandService.createBrand(request);
        ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>builder()
                .message("Create brand successfully")
                .result(brand)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(@Valid @ModelAttribute BrandUpdateRequest request) {
        BrandResponse brand = brandService.updateBrand(request);
        ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>builder()
                .message("Update brand successfully")
                .result(brand)
                .build();
        return ResponseEntity.ok(response);
    }
}

