package com.phananh.e_commerce.presentation.dto.response.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductSummaryResponse {
    private Long productId;
    private String productName;
    private BigDecimal minPrice;
    private String brandName;
    private String categoryName;
    private String avatarUrl;
}

