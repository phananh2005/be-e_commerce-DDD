package com.phananh.e_commerce.order.presentation.dto.response.cart;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartItemResponse {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private boolean productIsActive;
    private String currentVariantId;
    private String variantSkuCode;
    private String variantImageUrl;
    private BigDecimal variantPrice;
    private Integer stockQuantity;
    private Integer quantity;
}


