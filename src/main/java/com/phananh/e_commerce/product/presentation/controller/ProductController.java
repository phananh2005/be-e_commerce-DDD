package com.phananh.e_commerce.product.presentation.controller;

import com.phananh.e_commerce.modules.productcatalog.presentation.dto.request.product.*;
import com.phananh.e_commerce.product.presentation.dto.request.product.*;
import com.phananh.e_commerce.product.presentation.dto.response.product.ProductDetailResponse;
import com.phananh.e_commerce.product.presentation.dto.response.product.ProductSummaryResponse;
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
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Sản phẩm", description = "Các API quản lý thông tin sản phẩm")
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
        ProductDetailResponse product = productService.getProductById(id);
        ApiResponse<ProductDetailResponse> response = ApiResponse.<ProductDetailResponse>builder()
                .message("Get product successfully")
                .result(product)
                .build();
        return ResponseEntity.ok(response);
    }

    //staff
    @GetMapping("/staff/product/search")
    public ResponseEntity<ApiResponse<java.util.List<ProductSummaryResponse>>> getAllProductsBySearch(@ModelAttribute @Valid ProductSearchRequest productSearchRequest) {
        java.util.List<ProductSummaryResponse> products = productService.getAllProductsBySearch(productSearchRequest);
        ApiResponse<java.util.List<ProductSummaryResponse>> response = ApiResponse.<java.util.List<ProductSummaryResponse>>builder()
                .message("Get all products successfully")
                .result(products)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/staff/product/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createProduct(@ModelAttribute @Valid ProductCreateRequest productCreateRequest) {
        productService.createProduct(productCreateRequest);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Product created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/staff/variant/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createVariantImage(@ModelAttribute @Valid VariantImageCreateRequest variantImageCreateRequest) {
        productService.createVariantImage(variantImageCreateRequest);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Variant image created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/staff/product/update")
    public ResponseEntity<ApiResponse<Void>> updateProduct(@ModelAttribute @Valid ProductUpdateRequest productUpdateRequest) {
        productService.updateProduct(productUpdateRequest);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Product updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/staff/variant/image")
    public ResponseEntity<ApiResponse<Void>> updateVariantImage(@ModelAttribute @Valid VariantImageUpdateRequest variantImageUpdateRequest) {
        productService.updateVariantImage(variantImageUpdateRequest);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Variant image updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/staff/product/{productId}/{status}")
    public ResponseEntity<ApiResponse<Void>> updateProductStatus(@PathVariable Long productId, @PathVariable String status) {
        productService.updateProductStatus(productId, status);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Product status updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/staff/variant/{variantId}/{stockQuantity}")
    public ResponseEntity<ApiResponse<Void>> updateVariantStock(@PathVariable Long variantId, @PathVariable Integer stockQuantity) {
        productService.updateVariantStock(variantId, stockQuantity);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Variant stock updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}


