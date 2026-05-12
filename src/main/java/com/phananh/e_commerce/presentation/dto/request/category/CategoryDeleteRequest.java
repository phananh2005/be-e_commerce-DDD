package com.phananh.e_commerce.presentation.dto.request.category;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDeleteRequest {

    @NotNull(message = "Category id is required")
    private Long categoryId;

    @Builder.Default
    private Boolean hardDelete = false;
}



