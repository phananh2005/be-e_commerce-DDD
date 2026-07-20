package com.phananh.e_commerce.order.presentation.controller;

import com.phananh.e_commerce.order.application.dto.response.order.*;
import com.phananh.e_commerce.order.application.service.OrderService;
import com.phananh.e_commerce.order.presentation.dto.request.order.OrderFilterRequest;
import com.phananh.e_commerce.order.presentation.dto.request.order.OrderStatusUpdateRequest;
import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quản trị - Đơn hàng", description = "API quản trị đơn hàng")
public class ManagementOrderController {

    OrderService orderService;

    @Operation(summary = "Tìm kiếm đơn hàng", description = "Tìm kiếm và phân trang danh sách đơn hàng theo mã, họ tên, SĐT, địa chỉ, trạng thái")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ManagementOrderResponse>>> getAllOrdersForManagement(
            @ModelAttribute OrderFilterRequest filter) {
        Page<ManagementOrderResponse> response = orderService.getAllOrders(filter);
        return ResponseEntity.ok(ApiResponse.<Page<ManagementOrderResponse>>builder()
                .result(response)
                .message("Get orders successfully")
                .build());
    }

    @Operation(summary = "Lấy chi tiết đơn hàng", description = "Lấy thông tin chi tiết của một đơn hàng theo ID")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetailForManagement(@PathVariable Long orderId) {
        OrderDetailResponse response = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(ApiResponse.<OrderDetailResponse>builder()
                .result(response)
                .message("Get order detail successfully")
                .build());
    }

@Operation(summary = "Cập nhật trạng thái đơn hàng", description = "Cập nhật trạng thái xử lý của đơn hàng. Khi status là CANCELLED hoặc RETURNED thì phải nhập cancellationReason.")
    @PatchMapping("{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusUpdateRequest request) {
        orderService.updateOrderStatus(orderId, request.getStatus(), request.getCancellationReason());
        return ResponseEntity.noContent().build();
    }
}
