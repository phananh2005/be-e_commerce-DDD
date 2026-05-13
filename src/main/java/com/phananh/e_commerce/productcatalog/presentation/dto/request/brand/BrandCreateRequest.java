package com.phananh.e_commerce.productcatalog.presentation.dto.request.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandCreateRequest {

    @NotBlank(message = "Brand name is required")
    private String name;

    private MultipartFile image;
}


