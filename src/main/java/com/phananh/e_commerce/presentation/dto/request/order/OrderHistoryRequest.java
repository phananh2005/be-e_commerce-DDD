package com.phananh.e_commerce.presentation.dto.request.order;

import com.phananh.e_commerce.domain.model.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryRequest {

    private OrderStatus status;

    @Builder.Default
    @Min(value = 0, message = "Page must be >= 0")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "Size must be >= 1")
    private Integer size = 20;
}



