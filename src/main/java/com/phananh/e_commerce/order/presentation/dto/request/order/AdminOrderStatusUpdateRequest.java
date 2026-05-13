package com.phananh.e_commerce.order.presentation.dto.request.order;

import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderStatusUpdateRequest {

    @NotNull(message = "Order id is required")
    private Long orderId;

    @NotNull(message = "Order status is required")
    private OrderStatus status;

    private String note;
}



