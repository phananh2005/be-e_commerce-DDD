package com.phananh.e_commerce.presentation.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusTrackingRequest {

    @NotNull(message = "Order id is required")
    private Long orderId;
}



