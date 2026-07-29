package com.phananh.e_commerce.order.presentation.dto.request.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateItemRequest {

    @JsonIgnore
    @Deprecated
    private Long cartItemId;

    @JsonIgnore
    @Deprecated
    private Long variantId;

    @NotNull(message = "Cart item uuid is required")
    private String cartItemUuid;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be >= 1")
    private Integer quantity;

    @NotNull(message = "Variant uuid is required")
    private String variantUuid;
}



