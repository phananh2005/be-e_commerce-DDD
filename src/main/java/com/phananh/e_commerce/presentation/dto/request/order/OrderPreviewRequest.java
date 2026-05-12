package com.phananh.e_commerce.presentation.dto.request.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderPreviewRequest {

    @NotNull(message = "Variant id is required")
    private Long variantId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;
}

