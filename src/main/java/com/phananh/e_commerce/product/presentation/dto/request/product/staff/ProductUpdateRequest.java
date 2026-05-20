package com.phananh.e_commerce.product.presentation.dto.request.product.staff;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductUpdateRequest {

    @NotNull(message = "Product id is required")
    private Long productId;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Category id is required")
    private Long categoryId;

    @NotNull(message = "Brand id is required")
    private Long brandId;

    @NotNull(message = "Images are required")
    private MultipartFile avatar;

    @NotNull(message = "Variants are required")
    @Valid
    private List<VariantUpdateRequest> variants;

    @Data
    public static class VariantUpdateRequest {
        @NotNull(message = "Variant id is required")
        private Long variantId;

        @NotBlank(message = "SKU code is required")
        private String skuCode;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", message = "Price must be >= 0")
        private BigDecimal price;

        @NotNull(message = "Stock quantity is required")
        @PositiveOrZero(message = "Stock quantity must be >= 0")
        private Integer stockQuantity;

        private Map<String, String> attributes;
    }
}


