package com.phananh.e_commerce.order.application.service;

import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenuePeriodProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenueSummaryProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsOverviewProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsRangeProjection;
import com.phananh.e_commerce.order.application.dto.response.order.ManagementOrderResponse;
import com.phananh.e_commerce.order.application.dto.response.order.OrderDetailResponse;
import com.phananh.e_commerce.order.application.dto.response.order.OrderPreviewDetailResponse;
import com.phananh.e_commerce.order.application.dto.response.order.OrderSummaryResponse;
import com.phananh.e_commerce.order.presentation.dto.request.order.CheckoutRequest;
import com.phananh.e_commerce.order.presentation.dto.request.order.OrderFilterRequest;
import com.phananh.e_commerce.order.presentation.dto.request.order.OrderPreviewRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderStatisticsOverviewProjection getStatisticsOverview();
    OrderStatisticsRangeProjection getStatisticsByDateRange(LocalDateTime fromDate, LocalDateTime toDate);
    OrderRevenueSummaryProjection getRevenueSummary(LocalDateTime fromDate, LocalDateTime toDate);
    List<OrderRevenuePeriodProjection> getRevenueReport(LocalDateTime fromDate, LocalDateTime toDate, String groupBy);
    Page<ManagementOrderResponse> getAllOrders(OrderFilterRequest orderFilterRequest);

    OrderPreviewDetailResponse previewOrder(List<OrderPreviewRequest> orderPreviewRequest);

    void checkout(CheckoutRequest checkoutRequest);

    List<OrderSummaryResponse> getMyOrder();

    OrderDetailResponse getOrderDetail(Long orderId);

    OrderDetailResponse getOrderDetailForManagement(Long orderId);

    void updateOrderStatus(Long orderId, String status, String cancellationReason);
}


