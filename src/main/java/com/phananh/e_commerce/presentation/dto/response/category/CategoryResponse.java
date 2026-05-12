package com.phananh.e_commerce.presentation.dto.response.category;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CategoryResponse {
    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
}

