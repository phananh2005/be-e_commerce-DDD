package com.phananh.e_commerce.presentation.dto.request.category;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategorySearchRequest {

    private String keyword;

    @Builder.Default
    @Min(value = 0, message = "Page must be >= 0")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "Size must be >= 1")
    private Integer size = 20;
}

