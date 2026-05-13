package com.phananh.e_commerce.order.application.mapper;

import com.phananh.e_commerce.order.domain.model.entity.CartItem;
import com.phananh.e_commerce.order.presentation.dto.response.cart.CartItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "cartItemId", source = "id")
    @Mapping(target = "productId", source = "variant.product.id")
    @Mapping(target = "productName", source = "variant.product.name")
    @Mapping(target = "variantSkuCode", source = "variant.skuCode")
    @Mapping(target = "variantPrice", source = "variant.price")
    @Mapping(target = "stockQuantity", source = "variant.stockQuantity")
    @Mapping(target = "quantity", source = "quantity")
    CartItemResponse toResponse(CartItem cartItem);
}


