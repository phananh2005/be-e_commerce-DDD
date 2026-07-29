package com.phananh.e_commerce.product.application.dto.response.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDetailResponseForManagement {
    @JsonIgnore
    @Deprecated
    private Long id;
    private String uuid;
    private String name;
    private String description;
    private String avatarUrl;
    private String status;
    private Long categoryId;
    private Long brandId;
    private String createdBy;
    private LocalDateTime createdAt;
    private String modifiedBy;
    private LocalDateTime modifiedAt;
}
