package com.phananh.e_commerce.product.presentation.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInfoUpdateRequest {

    @NotNull(message = "Category id is required")
    private Long categoryId;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private String categoryDescription;
}


