package com.phananh.e_commerce.order.presentation.dto.request.order;

import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderFilterRequest {
    private String orderCode;
    private String fullName;
    private String phoneNumber;
    private String shippingAddress;
    private OrderStatus status;
    private LocalDateTime createdFromDate;
    private LocalDateTime createdToDate;

    @Min(value = 0, message = "Page must be >= 0")
    private Integer page;

    @Min(value = 1, message = "Size must be >= 1")
    private Integer size;

    private String sortBy;
    private String sortType;
}
