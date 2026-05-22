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
public class ProductCreateRequest {

    private String name;

    private String description;

    private Long categoryId;

    private Long brandId;

    private MultipartFile productAvatar;

    @Valid
    private List<VariantCreateRequest> variants;

    @Data
    public static class VariantCreateRequest {
        @NotBlank(message = "SKU code is required")
        private String skuCode;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", message = "Price must be >= 0")
        private BigDecimal price;

        @NotNull(message = "Stock quantity is required")
        @PositiveOrZero(message = "Stock quantity must be >= 0")
        private Integer stockQuantity;

        private Map<String, String> attributes;

        private MultipartFile variantAvatar;

        private List<MultipartFile> variantImages;
    }
}


