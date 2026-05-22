package com.phananh.e_commerce.productcatalog.presentation.dto.request.brand;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandStatusUpdateRequest {
    @NotNull(message = "Brand id is required")
    private Long brandId;

    @NotNull(message = "Brand status is required")
    private String status;
}
