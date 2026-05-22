package com.phananh.e_commerce.product.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductResponse;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductVariantResponse;
import com.phananh.e_commerce.product.application.service.StaffProductService;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.*;
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
@RequestMapping("/staff")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Sản phẩm", description = "Các API quản lý thông tin sản phẩm")
public class StaffProductController {

    StaffProductService staffProductService;

    @GetMapping("/product/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProductsBySearch(@ModelAttribute @Valid StaffProductSearchRequest staffProductSearchRequest) {
        Page<ProductResponse> products = staffProductService.getAllProductsBySearch(staffProductSearchRequest);
        ApiResponse<Page<ProductResponse>> response = ApiResponse.<Page<ProductResponse>>builder()
                .message("Get product successfully")
                .result(products)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getStaffProductById(@PathVariable Long id) {
        ProductResponse product = staffProductService.getStaffProductById(id);
        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .message("Get product successfully")
                .result(product)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}/variants")
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> getStaffProductVariantsByProductId(@PathVariable Long productId) {
        List<ProductVariantResponse> variants = staffProductService.getStaffProductVariantsByProductId(productId);
        ApiResponse<List<ProductVariantResponse>> response = ApiResponse.<List<ProductVariantResponse>>builder()
                .message("Get product variants successfully")
                .result(variants)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/product/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@ModelAttribute @Valid ProductCreateRequest productCreateRequest) {
        staffProductService.createProduct(productCreateRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/product/update")
    public ResponseEntity<ApiResponse<Void>> updateProduct(@ModelAttribute @Valid ProductUpdateRequest productUpdateRequest) {
        staffProductService.updateProduct(productUpdateRequest);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Product updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/variant/image")
    public ResponseEntity<ApiResponse<Void>> updateVariantImage(@ModelAttribute @Valid VariantImageUpdateRequest variantImageUpdateRequest) {
        staffProductService.updateVariantImage(variantImageUpdateRequest);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Variant image updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/product/{productId}/{status}")
    public ResponseEntity<ApiResponse<Void>> updateProductStatus(@PathVariable Long productId, @PathVariable String status) {
        staffProductService.updateProductStatus(productId, status);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Product status updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/variant/{variantId}/{stockQuantity}")
    public ResponseEntity<ApiResponse<Void>> updateVariantStock(@PathVariable Long variantId, @PathVariable Integer stockQuantity) {
        staffProductService.updateVariantStock(variantId, stockQuantity);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Variant stock updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
