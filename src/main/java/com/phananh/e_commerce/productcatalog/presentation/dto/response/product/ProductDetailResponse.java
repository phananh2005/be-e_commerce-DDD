package com.phananh.e_commerce.productcatalog.presentation.dto.response.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProductDetailResponse{

    private Long productId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private String avatarUrl;
    private String productName;
    private String description;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String status;
    private List<ProductVariant> variants;

    @Data
    @Builder
    public static class ProductVariant {
        private Long variantId;
        private String variantSkuCode;
        private BigDecimal variantPrice;
        private Integer stockQuantity;
        private List<Image> variantImageUrl;
        private List<Attribute> attributes;

        @Data
        @Builder
        public static class Attribute {
            private Long attributeId;
            private String attributeName;
            private String attributeValue;
        }

        @Data
        @Builder
        public static class Image {
            private Long imageId;
            private String imageUrl;
            private boolean isAvatar;
        }
    }
}


