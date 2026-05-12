package com.phananh.e_commerce.presentation.mapper;

import com.phananh.e_commerce.presentation.dto.response.cart.CartItemResponse;
import com.phananh.e_commerce.domain.model.entity.CartItem;
import com.phananh.e_commerce.domain.model.entity.ProductVariant;
import com.phananh.e_commerce.domain.model.entity.VariantImage;
import com.phananh.e_commerce.domain.model.enums.ProductStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.phananh.e_commerce.presentation.dto.response.product.ProductDetailResponse;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "cartItemId", source = "id")
    @Mapping(target = "productId", source = "variant.product.id")
    @Mapping(target = "productName", source = "variant.product.name")
    @Mapping(target = "productIsActive", expression = "java(productIsActive(cartItem.getVariant()))")
    @Mapping(target = "currentVariantId", source = "variant.id")
    @Mapping(target = "currentSkuCode", source = "variant.skuCode")
    @Mapping(target = "variantImageUrl", expression = "java(getAvatarImageUrl(cartItem.getVariant()))")
    @Mapping(target = "variantPrice", source = "variant.price")
    @Mapping(target = "currentQuantity", source = "quantity")
    CartItemResponse toResponse(CartItem cartItem);

    ProductDetailResponse.ProductVariant toVariantResponse(ProductVariant variant);

    default boolean productIsActive(ProductVariant variant) {
        return variant.getProduct().getStatus() == ProductStatus.ACTIVE;
    }

    default String getAvatarImageUrl(ProductVariant variant) {
        if (variant == null || variant.getImages() == null) {
            return null;
        }

        return variant.getImages().stream()
                .filter(VariantImage::isAvatar)
                .map(VariantImage::getImageUrl)
                .findFirst()
                .orElse(null);
    }
}

