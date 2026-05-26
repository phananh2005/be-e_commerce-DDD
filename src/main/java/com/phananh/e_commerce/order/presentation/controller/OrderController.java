package com.phananh.e_commerce.order.presentation.controller;

import com.phananh.e_commerce.order.application.dto.response.order.*;
import com.phananh.e_commerce.order.presentation.dto.request.order.CheckoutRequest;
import com.phananh.e_commerce.order.presentation.dto.request.order.OrderPreviewRequest;
import com.phananh.e_commerce.order.application.service.OrderService;
import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Đơn hàng", description = "Các API quản lý đơn hàng và trạng thái thanh toán")
public class OrderController {

    OrderService orderService;

    @GetMapping("/preview")
    public ResponseEntity<ApiResponse<OrderPreviewDetailResponse>> previewOrder(@RequestBody @Valid List<OrderPreviewRequest> orderPreviewRequest) {
        OrderPreviewDetailResponse response = orderService.previewOrder(orderPreviewRequest);
        return ResponseEntity.ok(ApiResponse.<OrderPreviewDetailResponse>builder()
                .result(response)
                .build());
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody @Valid CheckoutRequest checkoutRequest) {
        orderService.checkout(checkoutRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderSummaryResponse>>> getMyOrder() {
        List<OrderSummaryResponse> orderSummaryResponses = orderService.getMyOrder();
        return ResponseEntity.ok(ApiResponse.<List<OrderSummaryResponse>>builder()
                .result(orderSummaryResponses)
                .message("Get my orders successfully")
                .build());
    }

    @GetMapping("/my-orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(@PathVariable Long orderId) {
        OrderDetailResponse response = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(ApiResponse.<OrderDetailResponse>builder()
                .result(response)
                .message("Get order detail successfully")
                .build());
    }
}
