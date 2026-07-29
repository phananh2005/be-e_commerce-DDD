package com.phananh.e_commerce.order.application.dto.response.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {
    @JsonIgnore
    @Deprecated
    private Long cartItemId;

    private String cartItemUuid;
    private String productUuid;
    private String productName;
    private String productStatus;

    @JsonIgnore
    @Deprecated
    private String currentVariantId;
    private String currentVariantUuid;
    private String variantSkuCode;
    private String variantImageUrl;
    private BigDecimal variantPrice;
    private Integer stockQuantity;
    private Integer cartItemQuantity;
}


