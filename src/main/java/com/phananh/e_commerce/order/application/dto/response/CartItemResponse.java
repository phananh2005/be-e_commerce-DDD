package com.phananh.e_commerce.order.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private boolean productIsActive;
    private String currentVariantId;
    private String currentSkuCode;
    private String variantImageUrl;
    private BigDecimal variantPrice;
    private Integer currentQuantity;
}
