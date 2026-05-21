package com.phananh.e_commerce.productcatalog.presentation.dto.request.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BrandUpdateRequest {

    @NotNull(message = "Brand id is required")
    private Long brandId;

    @NotBlank(message = "Brand name is required")
    private String name;

    private String description;

    private MultipartFile image;
}

