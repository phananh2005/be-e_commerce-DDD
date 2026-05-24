package com.phananh.e_commerce.order.application.service;

import com.phananh.e_commerce.order.presentation.dto.request.order.CheckoutRequest;
import com.phananh.e_commerce.order.presentation.dto.request.order.OrderPreviewRequest;
import com.phananh.e_commerce.order.presentation.dto.response.order.CustomerOrderDetailResponse;
import com.phananh.e_commerce.order.presentation.dto.response.order.OrderPreviewDetailResponse;
import com.phananh.e_commerce.order.presentation.dto.response.order.OrderSummaryResponse;
import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<Order> getOrderById(Long orderId);

    List<Order> getOrdersByUsername(String username);

    Order createOrder(Order order);

    Order updateOrderStatus(Long orderId, String status);

    List<OrderItem> getOrderItems(Long orderId);

    OrderPreviewDetailResponse previewOrder(List<OrderPreviewRequest> orderPreviewRequest);

    void checkout(CheckoutRequest checkoutRequest);

    List<OrderSummaryResponse> getMyOrder();

    CustomerOrderDetailResponse getOrderDetail(Long orderId);
}


