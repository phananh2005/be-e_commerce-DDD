package com.phananh.e_commerce.presentation.dto.request.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventoryUpdateRequest {

    @NotNull(message = "Variant id is required")
    private Long variantId;

    @NotNull(message = "Quantity delta is required")
    private Integer quantityDelta;

    private String reason;
}



