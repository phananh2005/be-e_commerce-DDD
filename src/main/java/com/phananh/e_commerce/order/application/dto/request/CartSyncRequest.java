package com.phananh.e_commerce.order.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartSyncRequest {

    @Valid
    @NotEmpty(message = "Items must not be empty")
    private List<Item> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        @NotNull(message = "Variant id is required")
        private Long variantId;

        @NotNull(message = "Quantity is required")
        private Integer quantity;
    }
}
