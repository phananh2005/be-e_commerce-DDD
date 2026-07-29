package com.phananh.e_commerce.product.application.dto.response.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProductVariantResponseForManagement {
    @JsonIgnore
    @Deprecated
    private Long id;
    private String uuid;
    private String skuCode;
    private Double price;
    private Integer stockQuantity;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
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
