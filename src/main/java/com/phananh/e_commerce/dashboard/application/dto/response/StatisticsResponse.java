package com.phananh.e_commerce.dashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse {
    private Long totalUsers;
    private Long totalProducts;
    private Long totalOrders;
    private Long paidOrders;
    private Long pendingOrders;
    private BigDecimal totalRevenue;
}

