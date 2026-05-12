package com.phananh.e_commerce.application.service;

import com.phananh.e_commerce.presentation.dto.request.checkout.CheckoutRequest;
import com.phananh.e_commerce.presentation.dto.request.order.OrderPreviewRequest;
import com.phananh.e_commerce.presentation.dto.response.oder.CustomerOderDetailResponse;
import com.phananh.e_commerce.presentation.dto.response.oder.OrderSummaryResponse;
import com.phananh.e_commerce.presentation.dto.response.oder.OrderPreviewDetailResponse;

import java.util.List;

public interface OrderService {
    OrderPreviewDetailResponse previewOrder(List<OrderPreviewRequest> orderPreviewRequest);

	void checkout(CheckoutRequest checkoutRequest);

	List<OrderSummaryResponse> getMyOrder();

	CustomerOderDetailResponse getOrderDetail(Long orderId);

	void updateOrderStatus(Long orderId, String status);
}


