package com.phananh.e_commerce.order.application.mapper;

import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.model.OrderItem;
import com.phananh.e_commerce.order.presentation.dto.response.order.CustomerOrderDetailResponse;
import com.phananh.e_commerce.order.presentation.dto.response.order.OrderPreviewDetailResponse;
import com.phananh.e_commerce.order.presentation.dto.response.order.OrderSummaryResponse;
import com.phananh.e_commerce.product.application.dto.response.internal.ProductInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderSummaryResponse toOrderSummaryResponse(Order order) {
        return OrderSummaryResponse.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus() != null ? order.getStatus().toString() : null)
                .build();
    }

    public OrderSummaryResponse.Item toOrderSummaryItem(OrderItem orderItem, ProductInfoResponse productInfo) {
        return OrderSummaryResponse.Item.builder()
                .productName(productInfo.getProductName())
                .skuCode(productInfo.getVariantSkuCode())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }

    public CustomerOrderDetailResponse.Item toCustomerOrderDetailItem(OrderItem orderItem, ProductInfoResponse productInfo) {
        return CustomerOrderDetailResponse.Item.builder()
                .productId(productInfo.getProductId())
                .productName(productInfo.getProductName())
                .skuCode(productInfo.getVariantSkuCode())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }

    public CustomerOrderDetailResponse toCustomerOrderDetailResponse(Order order) {
        return CustomerOrderDetailResponse.builder()
                .orderId(order.getId())
                .createdAt(order.getCreatedAt())
                .modifiedAt(order.getModifiedAt())
                .createdBy(order.getCreatedBy())
                .modifiedBy(order.getModifiedBy())
                .fullName(order.getFullName())
                .isPaid(order.getIsPaid())
                .paymentDate(order.getPaymentDate())
                .paymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().toString() : null)
                .phoneNumber(order.getPhoneNumber())
                .shippingAddress(order.getShippingAddress())
                .shippingFee(order.getShippingFee())
                .status(order.getStatus() != null ? order.getStatus().toString() : null)
                .totalPrice(order.getTotalPrice())
                .build();
    }

    public OrderPreviewDetailResponse.Item toOrderPreviewItem(ProductInfoResponse productInfo) {
        return OrderPreviewDetailResponse.Item.builder()
                .skuCode(productInfo.getVariantSkuCode())
                .productId(productInfo.getProductId())
                .productName(productInfo.getProductName())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }

    public OrderPreviewDetailResponse toOrderPreviewDetailResponse(UserInfoResponse userInfo) {
        return OrderPreviewDetailResponse.builder()
                .fullName(userInfo.getFullName())
                .phoneNumber(userInfo.getPhoneNumber())
                .shippingAddress(userInfo.getAddress())
                .build();
    }
}


