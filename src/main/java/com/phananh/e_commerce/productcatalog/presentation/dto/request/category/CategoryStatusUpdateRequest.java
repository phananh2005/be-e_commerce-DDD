package com.phananh.e_commerce.productcatalog.presentation.dto.request.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryStatusUpdateRequest {
    @NotNull(message = "Category id is required")
    private Long categoryId;

    @NotNull(message = "Category status is required")
    private Boolean status;
}

