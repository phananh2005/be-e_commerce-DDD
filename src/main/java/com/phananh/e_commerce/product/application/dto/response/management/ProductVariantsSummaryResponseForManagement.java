package com.phananh.e_commerce.product.application.dto.response.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ProductVariantsSummaryResponseForManagement {
    @JsonIgnore
    @Deprecated
    private Long productId;
    private String productUuid;
    private List<Variant> variants;

    @Data
    public static class Variant {
        @JsonIgnore
        @Deprecated
        private Long variantId;
        private String variantUuid;
        private String skuCode;
        private Integer stockQuantity;
        private Double price;
        private String status;
        private String avatarImageUrl;
    }
}
