package com.phananh.e_commerce.product.application.dto.response.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSummaryResponse {
    @JsonIgnore
    @Deprecated
    private Long productId;
    private String productUuid;
    private String productName;
    private BigDecimal minPrice;
    private String avatarUrl;
}


