package com.phananh.e_commerce.presentation.dto.response.brand;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BrandResponse {
    private Long brandId;
    private String brandName;
    private String brandImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
}


