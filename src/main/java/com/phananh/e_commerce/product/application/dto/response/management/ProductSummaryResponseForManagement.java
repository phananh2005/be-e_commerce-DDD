package com.phananh.e_commerce.product.application.dto.response.management;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductSummaryResponseForManagement {
    private Long id;
    private String uuid;
    private String avatarUrl;
    private String name;
    private String status;
    private LocalDateTime modifiedAt;
}
