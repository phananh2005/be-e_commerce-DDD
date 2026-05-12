package com.phananh.e_commerce.application.service.impl;

import com.phananh.e_commerce.presentation.dto.request.checkout.CheckoutRequest;
import com.phananh.e_commerce.presentation.dto.request.order.OrderPreviewRequest;
import com.phananh.e_commerce.presentation.dto.response.oder.CustomerOderDetailResponse;
import com.phananh.e_commerce.presentation.dto.response.oder.OrderSummaryResponse;
import com.phananh.e_commerce.presentation.dto.response.oder.OrderPreviewDetailResponse;
import com.phananh.e_commerce.application.exception.AppException;
import com.phananh.e_commerce.application.exception.ErrorCode;
import com.phananh.e_commerce.presentation.mapper.OrderMapper;
import com.phananh.e_commerce.domain.model.entity.Order;
import com.phananh.e_commerce.domain.model.entity.OrderItem;
import com.phananh.e_commerce.domain.model.entity.ProductVariant;
import com.phananh.e_commerce.domain.model.entity.User;
import com.phananh.e_commerce.domain.model.enums.OrderStatus;
import com.phananh.e_commerce.domain.model.enums.PaymentMethod;
import com.phananh.e_commerce.infrastructure.persistence.repository.OrderRepository;
import com.phananh.e_commerce.infrastructure.persistence.repository.ProductVariantRepository;
import com.phananh.e_commerce.infrastructure.persistence.repository.UserRepository;
import com.phananh.e_commerce.application.service.OrderService;
import com.phananh.e_commerce.infrastructure.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;
    ProductVariantRepository productVariantRepository;

    OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public OrderPreviewDetailResponse previewOrder(List<OrderPreviewRequest> orderPreviewRequest) {
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        OrderPreviewDetailResponse response = orderMapper.toOrderPreviewDetailResponse(user, Arrays.stream(PaymentMethod.values()).toList());

        List<OrderPreviewDetailResponse.Item> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderPreviewRequest request : orderPreviewRequest) {
            ProductVariant variant = productVariantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
            Integer quantity = request.getQuantity();
            items.add(orderMapper.toOrderPreviewItemResponse(variant, quantity));
            totalPrice = totalPrice.add(variant.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }
        response.setItems(items);

        BigDecimal shippingFee = totalPrice.compareTo(BigDecimal.valueOf(1000000)) < 0 ? BigDecimal.valueOf(50000) : BigDecimal.ZERO;

        response.setShippingFee(shippingFee);
        response.setTotalPrice(totalPrice.add(shippingFee));

        return response;
    }

    @Override
    @Transactional
    public void checkout(CheckoutRequest checkoutRequest) {
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CheckoutRequest.Item item : checkoutRequest.getItems()) {

            ProductVariant variant = productVariantRepository.findById(item.getVariantId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

            if(variant.getStockQuantity() <= 0) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            if (variant.getStockQuantity() < item.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }

            Integer quantity = item.getQuantity();
            totalPrice = totalPrice.add(variant.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        BigDecimal shippingFee = totalPrice.compareTo(BigDecimal.valueOf(1000000)) < 0 ? BigDecimal.valueOf(50000) : BigDecimal.ZERO;

        Order order = orderMapper.toOrderEntity(checkoutRequest);
        order.setPaid(true);
        order.setPaymentDate(LocalDateTime.now());
        order.setShippingFee(shippingFee);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(totalPrice.add(shippingFee));
        order.setUser(user);

        Set<OrderItem> orderItems = checkoutRequest.getItems().stream().map(item -> {
            ProductVariant variant = productVariantRepository.findById(item.getVariantId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

            return OrderItem.builder()
                    .price(variant.getPrice())
                    .quantity(item.getQuantity())
                    .order(order)
                    .variant(variant)
                    .build();
        }).collect(Collectors.toSet());
        order.setOrderItems(orderItems);

        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> getMyOrder() {
        List<Order> orders = orderRepository.findByUser_Name(SecurityUtils.getCurrentUserName());

        if(orders.isEmpty()) return Collections.emptyList();

        List<OrderSummaryResponse> orderSummaryResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderSummaryResponse response = orderMapper.toCustomerOrderSummaryResponse(order);

            List<OrderItem> orderItems = order.getOrderItems().stream().toList();
            if(orderItems.isEmpty()) throw new AppException(ErrorCode.ORDER_ITEM_NOT_FOUND);

            List<OrderSummaryResponse.Item> items = orderItems.stream()
                    .map(orderMapper::toOrderSummaryItemResponse)
                    .collect(Collectors.toList());
            response.setItems(items);

            orderSummaryResponses.add(response);
        }

        return orderSummaryResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerOderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderItem> orderItems = order.getOrderItems().stream().toList();
        if(orderItems.isEmpty()) throw new AppException(ErrorCode.ORDER_ITEM_NOT_FOUND);

        CustomerOderDetailResponse orderDetailResponse = orderMapper.toCustomerOrderDetailResponse(order);
        orderDetailResponse.setItems(orderItems.stream().map(orderMapper::toCustomerOrderDetailItemResponse).collect(Collectors.toList()));
        return orderDetailResponse;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(orderStatus);
            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
    }
}


