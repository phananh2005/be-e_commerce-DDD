package com.phananh.e_commerce.order.presentation.dto.request.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequest {

    @NotBlank(message = "Receiver name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotNull(message = "Shipping fee is required")
    private BigDecimal shippingFee;

    @Valid
    @NotEmpty(message = "Items are required")
    private List<Item> items;

    @Data
    public static class Item {

        @JsonIgnore
        @Deprecated
        private Long variantId;

        @NotNull(message = "Variant uuid is required")
        private String variantUuid;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be >= 1")
        private Integer quantity;
    }
}