package com.phananh.e_commerce.presentation.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class CategoryCreateRequest {

    @NotBlank(message = "Category name is required")
    private String categoryName;

    @NotBlank(message = "Category description is required")
    private String categoryDescription;

    private MultipartFile image;
}



