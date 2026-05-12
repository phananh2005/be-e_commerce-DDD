package com.phananh.e_commerce.presentation.mapper;

import com.phananh.e_commerce.presentation.dto.request.checkout.CheckoutRequest;
import com.phananh.e_commerce.presentation.dto.response.oder.CustomerOderDetailResponse;
import com.phananh.e_commerce.presentation.dto.response.oder.OrderSummaryResponse;
import com.phananh.e_commerce.presentation.dto.response.oder.OrderPreviewDetailResponse;
import com.phananh.e_commerce.domain.model.enums.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "shippingAddress", source = "user.address")
    @Mapping(target = "paymentMethods", source = "paymentMethods")
    OrderPreviewDetailResponse toOrderPreviewDetailResponse(User user, List<PaymentMethod> paymentMethods);

    @Mapping(target = "productId", source = "variant.product.id")
    @Mapping(target = "productName", source = "variant.product.name")
    @Mapping(target = "skuCode", source = "variant.skuCode")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "variant.price")
    @Mapping(target = "variantImageUrl", expression = "java(getAvatarUrl(variant))")
    OrderPreviewDetailResponse.Item toOrderPreviewItemResponse(ProductVariant variant, Integer quantity);

    default String getAvatarUrl(ProductVariant variant) {
        return variant.getImages().stream()
                .filter(VariantImage::isAvatar)
                .findFirst()
                .map(VariantImage::getImageUrl)
                .orElse(null);
    }


    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    Order toOrderEntity(CheckoutRequest checkoutRequest);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "status", source = "status")
    OrderSummaryResponse toCustomerOrderSummaryResponse(Order order);

    @Mapping(target = "productName", source = "variant.product.name")
    @Mapping(target = "skuCode", source = "variant.skuCode")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "variantImageUrl", expression = "java(getAvatarUrl(orderItem))")
    OrderSummaryResponse.Item toOrderSummaryItemResponse(OrderItem orderItem);

    default String getAvatarUrl(OrderItem orderItem) {
        return orderItem.getVariant()
                .getImages().stream()
                .filter(VariantImage::isAvatar)
                .findFirst()
                .map(VariantImage::getImageUrl)
                .orElse(null);
    }


    CustomerOderDetailResponse toCustomerOrderDetailResponse(Order order);

    @Mapping(target = "productId", source = "variant.product.id")
    @Mapping(target = "productName", source = "variant.product.name")
    @Mapping(target = "skuCode", source = "variant.skuCode")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "variantImageUrl", expression = "java(getAvatarUrl(orderItem))")
    CustomerOderDetailResponse.Item toCustomerOrderDetailItemResponse(OrderItem orderItem);
}

