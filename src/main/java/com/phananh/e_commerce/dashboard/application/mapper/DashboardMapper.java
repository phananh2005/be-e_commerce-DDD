package com.phananh.e_commerce.dashboard.application.mapper;

import com.phananh.e_commerce.dashboard.application.dto.response.DashboardResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.SalesStatsResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.StatisticsResponse;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenuePeriodProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenueSummaryProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsOverviewProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsRangeProjection;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DashboardMapper {

    default StatisticsResponse toStatisticsResponse(long totalUsers,
                                                    long totalProducts,
                                                    OrderStatisticsOverviewProjection orderStats) {
        return StatisticsResponse.builder()
                .totalUsers(totalUsers)
                .totalProducts(totalProducts)
                .totalOrders(orderStats.getTotalOrders())
                .paidOrders(orderStats.getPaidOrders())
                .pendingOrders(orderStats.getPendingOrders())
                .totalRevenue(orderStats.getTotalRevenue())
                .build();
    }

    default DashboardResponse.StatusStatistic toStatusStatistic(String status, long count) {
        return DashboardResponse.StatusStatistic.builder()
                .status(status)
                .count(count)
                .build();
    }

    default List<DashboardResponse.StatusStatistic> toStatusStatistics(OrderStatisticsRangeProjection projection) {
        return List.of(
                toStatusStatistic("PENDING", safeLong(projection.getPendingOrders())),
                toStatusStatistic("CONFIRMED", safeLong(projection.getConfirmedOrders())),
                toStatusStatistic("SHIPPING", safeLong(projection.getShippingOrders())),
                toStatusStatistic("DELIVERED", safeLong(projection.getDeliveredOrders())),
                toStatusStatistic("CANCELLED", safeLong(projection.getCancelledOrders())),
                toStatusStatistic("RETURNED", safeLong(projection.getReturnedOrders()))
        );
    }

    default DashboardResponse toDashboardResponse(LocalDate fromDate,
                                                  LocalDate toDate,
                                                  OrderStatisticsRangeProjection orderStats,
                                                  List<DashboardResponse.StatusStatistic> statusStatistics) {
        return DashboardResponse.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .totalOrders(orderStats.getTotalOrders())
                .paidOrders(orderStats.getPaidOrders())
                .pendingOrders(orderStats.getPendingOrders())
                .confirmedOrders(orderStats.getConfirmedOrders())
                .shippingOrders(orderStats.getShippingOrders())
                .deliveredOrders(orderStats.getDeliveredOrders())
                .cancelledOrders(orderStats.getCancelledOrders())
                .returnedOrders(orderStats.getReturnedOrders())
                .totalRevenue(orderStats.getTotalRevenue())
                .statusStatistics(statusStatistics)
                .build();
    }

    default SalesStatsResponse.Item toSalesItem(OrderRevenuePeriodProjection projection) {
        return SalesStatsResponse.Item.builder()
                .period(projection.getPeriod())
                .orders(safeLong(projection.getOrders()))
                .paidOrders(safeLong(projection.getPaidOrders()))
                .revenue(projection.getRevenue())
                .build();
    }

    default SalesStatsResponse toSalesStatsResponse(String groupBy,
                                                    LocalDate fromDate,
                                                    LocalDate toDate,
                                                    OrderRevenueSummaryProjection summary,
                                                    List<SalesStatsResponse.Item> items) {
        return SalesStatsResponse.builder()
                .groupBy(groupBy)
                .fromDate(fromDate)
                .toDate(toDate)
                .totalOrders(summary.getTotalOrders())
                .paidOrders(summary.getPaidOrders())
                .totalRevenue(summary.getTotalRevenue())
                .items(items)
                .build();
    }

    private long safeLong(Long value) {
        return value == null ? 0L : value;
    }
}

