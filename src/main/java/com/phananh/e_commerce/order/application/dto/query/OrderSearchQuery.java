package com.phananh.e_commerce.order.application.dto.query;

import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderSearchQuery {
    private String orderCode;
    private String fullName;
    private String phoneNumber;
    private String shippingAddress;
    private OrderStatus status;
    private LocalDateTime createdFromDate;
    private LocalDateTime createdToDate;
    private Pageable pageable;
}
