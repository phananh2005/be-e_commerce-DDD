package com.phananh.e_commerce.product.application.dto.response.management;

import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class ProductSummaryResponseForManagement {
    @JsonIgnore
    @Deprecated
    private Long id;
    private String uuid;
    private String avatarUrl;
    private String name;
    private String status;
    private String createdBy;
    private LocalDateTime createdAt;
    private String modifiedBy;
    private LocalDateTime modifiedAt;
}
