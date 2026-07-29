package com.phananh.e_commerce.order.presentation.dto.request.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPreviewRequest {

    @JsonIgnore
    @Deprecated
    private Long variantId;

    @NotNull(message = "Variant uuid is required")
    private String variantUuid;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be >= 1")
    private Integer quantity;
}


