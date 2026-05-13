package com.phananh.e_commerce.order.presentation.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPreviewRequest {

    @NotNull(message = "Variant id is required")
    private Long variantId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;
}


