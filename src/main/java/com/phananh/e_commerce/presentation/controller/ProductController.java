package com.phananh.e_commerce.presentation.controller;

import com.phananh.e_commerce.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.presentation.dto.response.product.ProductDetailResponse;
import com.phananh.e_commerce.presentation.dto.response.product.ProductSummaryResponse;
import com.phananh.e_commerce.application.service.ProductService;
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
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Sáº£n pháº©m", description = "CÃ¡c API quáº£n lÃ½ thÃ´ng tin sáº£n pháº©m")
public class ProductController {

    ProductService productService;

    //customer
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductSummaryResponse>>> getProductsActiveBySearch(@ModelAttribute @Valid ProductSearchRequest productSearchRequest) {
        Page<ProductSummaryResponse> productSummaryResponses = productService.getProductsActiveBySearch(productSearchRequest);
        ApiResponse<Page<ProductSummaryResponse>> response = ApiResponse.<Page<ProductSummaryResponse>>builder()
                .message("Get list product successfully")
                .result(productSummaryResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(@PathVariable Long id) {
        return null;
    }
    //so sÃ¡nh 2 sp
    //private Long variantId;
    //        private String skuCode;
    //        private Integer stockQuantity;


    //staff
    @GetMapping("/staff/product/search")
    public ResponseEntity<ApiResponse<ProductSummaryResponse>> getAllProductsBySearch(@ModelAttribute @Valid ProductSearchRequest productSearchRequest) {
        return null;
    }

    @PostMapping(value = "/staff/product/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createProduct(@ModelAttribute @Valid ProductCreateRequest productCreateRequest) {
        return null;
    }

    @PostMapping(value = "/staff/variant/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createVariantImage(@ModelAttribute @Valid VariantImageCreateRequest variantImageCreateRequest) {
        return null;
    }

    @PutMapping("/staff/product/update")
    public ResponseEntity<ApiResponse<Void>> updateProduct(@ModelAttribute @Valid ProductUpdateRequest productUpdateRequest) {
        return null;
    }

    @PutMapping("/staff/variant/image")
    public ResponseEntity<ApiResponse<Void>> updateVariantImage(@ModelAttribute @Valid VariantImageUpdateRequest variantImageUpdateRequest) {
        return null;
    }

    @PatchMapping("/staff/product/{productId}/{status}")
    public ResponseEntity<ApiResponse<Void>> updateProductStatus(@PathVariable Long productId, @PathVariable String status) {
        return null;
    }

    @PatchMapping("/staff/variant/{variantId}/{stockQuantity}")
    public ResponseEntity<ApiResponse<Void>> updateVariantStock(@PathVariable Long variantId, @PathVariable Integer stockQuantity) {
        return null;
    }
}

