package com.phananh.e_commerce.product.presentation.dto.request.brand;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BrandImageUpdateRequest {

    @NotNull(message = "Brand id is required")
    private Long brandId;

    private MultipartFile image;
}
