package com.phananh.e_commerce.product.application.dto.response.management;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDetailResponseForManagement {
    private Long id;
    private String uuid;
    private String name;
    private String description;
    private String avatarUrl;
    private String status;
    private String categoryName;
    private String brandName;
    private String createdBy;
    private LocalDateTime createdAt;
    private String modifiedBy;
    private LocalDateTime modifiedAt;
}
