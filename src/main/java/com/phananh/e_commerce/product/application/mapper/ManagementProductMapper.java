package com.phananh.e_commerce.product.application.mapper;

import com.phananh.e_commerce.product.application.dto.response.management.ProductResponse;
import com.phananh.e_commerce.product.application.dto.response.management.ProductVariantResponse;
import com.phananh.e_commerce.product.domain.model.AttributeValue;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import com.phananh.e_commerce.product.domain.model.VariantImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ManagementProductMapper {

    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    @Mapping(target = "status", expression = "java(product.getStatus() == null ? null : product.getStatus().name())")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    ProductResponse toManagementProductResponse(Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "skuCode", source = "skuCode")
    @Mapping(target = "price", expression = "java(productVariant.getPrice() == null ? null : productVariant.getPrice().doubleValue())")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "modifiedBy", source = "modifiedBy")
    @Mapping(target = "variantImageUrl", expression = "java(imagesToManagementImageSet(productVariant.getImages()))")
    @Mapping(target = "attributes", expression = "java(attributesToManagementAttributeSet(productVariant.getAttributeValues()))")
    ProductVariantResponse toManagementProductVariantResponse(ProductVariant productVariant);

    // Mapper for VariantImage -> management ProductVariantResponse.Image
    @Mapping(source = "id", target = "imageId")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "avatar", target = "avatar")
    ProductVariantResponse.Image toManagementVariantImageResponse(VariantImage image);

    // Convert Set<VariantImage> to Set<ProductVariantResponse.Image>
    default Set<ProductVariantResponse.Image> imagesToManagementImageSet(Set<VariantImage> images) {
        if (images == null) {
            return Set.of();
        }
        return images.stream()
                .map(this::toManagementVariantImageResponse)
                .collect(Collectors.toSet());
    }

    // Mapper for AttributeValue -> management ProductVariantResponse.Attribute
    @Mapping(source = "attribute.id", target = "attributeId")
    @Mapping(source = "attribute.name", target = "attributeName")
    @Mapping(source = "value", target = "attributeValue")
    ProductVariantResponse.Attribute toManagementAttributeResponse(AttributeValue attributeValue);

    // Convert Set<AttributeValue> to Set<ProductVariantResponse.Attribute>
    default Set<ProductVariantResponse.Attribute> attributesToManagementAttributeSet(Set<AttributeValue> attributes) {
        if (attributes == null) {
            return Set.of();
        }
        return attributes.stream()
                .map(this::toManagementAttributeResponse)
                .collect(Collectors.toSet());
    }
}



