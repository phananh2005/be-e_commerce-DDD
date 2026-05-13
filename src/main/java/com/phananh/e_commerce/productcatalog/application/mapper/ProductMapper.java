package com.phananh.e_commerce.productcatalog.application.mapper;

import com.phananh.e_commerce.productcatalog.presentation.dto.response.product.ProductDetailResponse;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.product.ProductSummaryResponse;
import com.phananh.e_commerce.productcatalog.domain.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "productName")
    @Mapping(target = "minPrice", expression = "java(product.getVariants().stream().map(com.phananh.e_commerce.modules.productcatalog.domain.model.entity.ProductVariant::getPrice).min(java.math.BigDecimal::compareTo).orElse(java.math.BigDecimal.ZERO))")
    @Mapping(source = "brand.name", target = "brandName")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    ProductSummaryResponse toProductSummaryResponse(Product product);

    ProductDetailResponse toProductDetailResponse(Product product);
}


