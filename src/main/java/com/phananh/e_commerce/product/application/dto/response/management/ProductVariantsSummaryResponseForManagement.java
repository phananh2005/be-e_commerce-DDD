package com.phananh.e_commerce.product.application.dto.response.management;

import lombok.Data;

import java.util.List;

@Data
public class ProductVariantsSummaryResponseForManagement {
    private Long productId;
    private List<Variant> variants;

    @Data
    public static class Variant {
        private Long variantId;
        private String skuCode;
        private Integer stockQuantity;
        private Double price;
        private String avatarImageUrl;
    }
}
