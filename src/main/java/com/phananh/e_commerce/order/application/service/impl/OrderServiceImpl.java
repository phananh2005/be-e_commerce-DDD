package com.phananh.e_commerce.order.application.service.impl;

import com.phananh.e_commerce.order.domain.model.entity.Order;
import com.phananh.e_commerce.order.domain.model.entity.OrderItem;
import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import com.phananh.e_commerce.order.infrastructure.persistence.repository.OrderRepository;
import com.phananh.e_commerce.order.infrastructure.persistence.repository.OrderItemRepository;
import com.phananh.e_commerce.order.application.service.OrderService;
import com.phananh.e_commerce.order.presentation.dto.request.order.CheckoutRequest;
import com.phananh.e_commerce.order.presentation.dto.request.order.OrderPreviewRequest;
import com.phananh.e_commerce.order.presentation.dto.response.order.CustomerOrderDetailResponse;
import com.phananh.e_commerce.order.presentation.dto.response.order.OrderPreviewDetailResponse;
import com.phananh.e_commerce.order.presentation.dto.response.order.OrderSummaryResponse;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    ProductVariantRepository productVariantRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUser_Username(username);
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        if (order.getTotalPrice() == null || order.getTotalPrice().signum() < 0) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        
        try {
            order.setStatus(OrderStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrder_Id(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPreviewDetailResponse previewOrder(List<OrderPreviewRequest> orderPreviewRequest) {
        // TODO: Implement preview order logic
        return null;
    }

    @Override
    @Transactional
    public void checkout(CheckoutRequest checkoutRequest) {
        // TODO: Implement checkout logic
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> getMyOrder() {
        // TODO: Implement get my order logic
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerOrderDetailResponse getOrderDetail(Long orderId) {
        // TODO: Implement get order detail logic
        return null;
    }
}


