package com.phananh.e_commerce.presentation.dto.request.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchRequest {

    private String keyword;

    private Long categoryId;

    private Long brandId;

    @DecimalMin(value = "0.0", message = "Min price must be >= 0")
    private double minPrice;

    @DecimalMin(value = "0.0", message = "Max price must be >= 0")
    private double maxPrice;

    @Min(value = 0, message = "Min rating must be >= 0")
    @Max(value = 5, message = "Min rating must be <= 5")
    private Integer minRating;

    @Builder.Default
    @Min(value = 0, message = "Page must be >= 0")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "Size must be >= 1")
    private Integer size = 20;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "DESC";
}



