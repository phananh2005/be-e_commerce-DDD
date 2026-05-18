package com.phananh.e_commerce.product.presentation.dto.request.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryImageUpdateRequest {

    @NotNull(message = "Category id is required")
    private Long categoryId;

    private MultipartFile image;
}

