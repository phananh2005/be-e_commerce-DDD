package com.phananh.e_commerce.order.presentation.dto.request.order;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusUpdateRequest {
    @NotBlank(message = "Status must not be blank")
    String status;

    String cancellationReason;
}
