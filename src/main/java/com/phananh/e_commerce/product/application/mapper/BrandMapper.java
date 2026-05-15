package com.phananh.e_commerce.product.application.mapper;

import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.brand.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "brandId", source = "id")
    @Mapping(target = "brandName", source = "name")
    @Mapping(target = "brandImage", source = "imageUrl")
    BrandResponse toResponse(Brand brand);
}


