package com.phananh.e_commerce.product.application.dto.response.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProductDetailResponse{

    @JsonIgnore
    @Deprecated
    private Long productId;
    private String productUuid;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private String avatarUrl;
    private String productName;
    private String productDescription;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private ProductStatus status;
    private Set<ProductVariantDetail> variants;

    @Data
    public static class ProductVariantDetail {
        @JsonIgnore
        @Deprecated
        private Long variantId;
        private String variantUuid;
        private String variantSkuCode;
        private BigDecimal variantPrice;
        private Integer stockQuantity;
        private String status;
        private Set<Image> variantImageUrl;
        private Set<Attribute> attributes;

        @Data
        public static class Attribute {
            private Long attributeId;
            private String attributeName;
            private String attributeValue;
        }

        @Data
        public static class Image {
            @JsonIgnore
            @Deprecated
            private Long imageId;
            private String imageUuid;
            private String imageUrl;
            private boolean isAvatar;
        }
    }
}


