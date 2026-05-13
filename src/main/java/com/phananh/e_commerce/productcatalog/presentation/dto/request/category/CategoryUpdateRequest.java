package com.phananh.e_commerce.productcatalog.presentation.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {

    @NotNull(message = "Category id is required")
    private Long categoryId;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    @NotBlank(message = "Category description is required")
    private String categoryDescription;

    private MultipartFile image;
}


