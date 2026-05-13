package com.phananh.e_commerce.order.presentation.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderApprovalRequest {

    @NotNull(message = "Order id is required")
    private Long orderId;

    @NotNull(message = "Approved flag is required")
    private Boolean approved;

    private String note;
}



