package com.phananh.e_commerce.presentation.dto.response.cart;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import com.phananh.e_commerce.presentation.dto.response.product.ProductDetailResponse;

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
    private List<ProductDetailResponse.ProductVariant> variants;
}

