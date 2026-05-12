package com.phananh.e_commerce.presentation.controller;

import com.phananh.e_commerce.presentation.dto.request.checkout.CheckoutRequest;
import com.phananh.e_commerce.presentation.dto.request.order.OrderPreviewRequest;
import com.phananh.e_commerce.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.presentation.dto.response.oder.CustomerOderDetailResponse;
import com.phananh.e_commerce.presentation.dto.response.oder.OrderSummaryResponse;
import com.phananh.e_commerce.presentation.dto.response.oder.OrderPreviewDetailResponse;
import com.phananh.e_commerce.application.service.OrderService;
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
@Tag(name = "ÄÆ¡n hÃ ng", description = "CÃ¡c API quáº£n lÃ½ Ä‘Æ¡n hÃ ng vÃ  tráº¡ng thÃ¡i thanh toÃ¡n")
public class OrderController {

    OrderService orderService;

    //customer
    @GetMapping("/preview") //Ä‘Æ°a ra hÃ³a Ä‘Æ¡n tÃ­nh tiá»n trÆ°á»›c khi thanh toÃ¡n
    public ResponseEntity<ApiResponse<OrderPreviewDetailResponse>> previewOrder(@RequestBody @Valid List<OrderPreviewRequest> orderPreviewRequest) {
        OrderPreviewDetailResponse orderPreviewDetailResponse =  orderService.previewOrder(orderPreviewRequest);
        return ResponseEntity.ok(ApiResponse.<OrderPreviewDetailResponse>builder()
                .result(orderPreviewDetailResponse)
                .build());
    }

    @PatchMapping("/checkout")
    public ResponseEntity<ApiResponse<Void>> checkout(@RequestBody @Valid CheckoutRequest checkoutRequest) {
        orderService.checkout(checkoutRequest);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Checkout successfully")
                .build());
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderSummaryResponse>>> getMyOrder() {
        List<OrderSummaryResponse> orderSummaryResponses = orderService.getMyOrder();
        return ResponseEntity.ok(ApiResponse.<List<OrderSummaryResponse>>builder()
                .result(orderSummaryResponses)
                .message("Get my orders successfully")
                .build());
    }

    @GetMapping("{orderId}")
    public ResponseEntity<ApiResponse<CustomerOderDetailResponse>> getOrderDetail(@PathVariable Long orderId) {
        CustomerOderDetailResponse customerOderDetailResponse = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(ApiResponse.<CustomerOderDetailResponse>builder()
                .result(customerOderDetailResponse)
                .message("Get order detail successfully")
                .build());
    }

    //staff
    @PatchMapping("{orderId}/{status}")
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(@PathVariable Long orderId, @PathVariable String status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Update order status successfully")
                .build());
    }
}

