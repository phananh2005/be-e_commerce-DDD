package com.phananh.e_commerce.order.application.dto.query;

import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderSearchQuery {
    private String orderUuid;
    private LocalDateTime createdFromDate;
    private LocalDateTime createdToDate;
    private OrderStatus status;
    private Long userId;
    private Pageable pageable;
}
