package com.phananh.e_commerce.order.application.mapper;

import com.phananh.e_commerce.order.application.dto.response.order.*;
import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.model.OrderItem;
import com.phananh.e_commerce.product.application.dto.response.internal.ProductInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().toString() : null)")
    @Mapping(target = "items", ignore = true)
    OrderSummaryResponse toOrderSummaryResponse(Order order);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "paymentDate", source = "paymentDate")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().toString() : null)")
    @Mapping(target = "isPaid", source = "isPaid")
    OrderStatisticsResponse toOrderStatisticsResponse(Order order);

    default OrderSummaryResponse.Item toOrderSummaryItem(OrderItem orderItem, ProductInfoResponse productInfo) {
        return OrderSummaryResponse.Item.builder()
                .productName(productInfo.getProductName())
                .skuCode(productInfo.getVariantSkuCode())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }

    default OrderDetailResponse.Item toCustomerOrderDetailItem(OrderItem orderItem, ProductInfoResponse productInfo) {
        return OrderDetailResponse.Item.builder()
                .productId(productInfo.getProductId())
                .productName(productInfo.getProductName())
                .skuCode(productInfo.getVariantSkuCode())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "shippingFee", source = "shippingFee")
    @Mapping(target = "addressInfo.fullName", source = "fullName")
    @Mapping(target = "addressInfo.phoneNumber", source = "phoneNumber")
    @Mapping(target = "addressInfo.shippingAddress", source = "shippingAddress")
    @Mapping(target = "isPaid", source = "isPaid")
    @Mapping(target = "paymentDate", source = "paymentDate")
    @Mapping(target = "paymentMethod", expression = "java(order.getPaymentMethod() != null ? order.getPaymentMethod().toString() : null)")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().toString() : null)")
    @Mapping(target = "cancellationReason", expression = "java(isCancelledOrReturned(order) ? order.getCancellationReason() : null)")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    @Mapping(target = "modifiedBy", source = "modifiedBy")
    @Mapping(target = "items", ignore = true)
    OrderDetailResponse toCustomerOrderDetailResponse(Order order);

    default boolean isCancelledOrReturned(Order order) {
        return order.getStatus() != null && 
               (order.getStatus().toString().equals("CANCELLED") || 
                order.getStatus().toString().equals("RETURNED"));
    }

    default OrderPreviewDetailResponse.Item toOrderPreviewItem(ProductInfoResponse productInfo) {
        return OrderPreviewDetailResponse.Item.builder()
                .skuCode(productInfo.getVariantSkuCode())
                .productId(productInfo.getProductId())
                .productName(productInfo.getProductName())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }

    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "shippingAddress", source = "address")
    @Mapping(target = "paymentMethods", ignore = true)
    @Mapping(target = "shippingFee", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "items", ignore = true)
    OrderPreviewDetailResponse toOrderPreviewDetailResponse(UserInfoResponse userInfo);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "userId", source = "order.userId")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "totalPrice", source = "order.totalPrice")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().toString() : null)")
    @Mapping(target = "createdAt", source = "order.createdAt")
    @Mapping(target = "modifiedAt", source = "order.modifiedAt")
    @Mapping(target = "items", ignore = true)
    ManagementOrderResponse toManagementOrderResponse(Order order, String username);

    default ManagementOrderResponse.Item toManagementOrderItem(OrderItem orderItem, ProductInfoResponse productInfo) {
        return ManagementOrderResponse.Item.builder()
                .productId(productInfo.getProductId())
                .productName(productInfo.getProductName())
                .skuCode(productInfo.getVariantSkuCode())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }
}


