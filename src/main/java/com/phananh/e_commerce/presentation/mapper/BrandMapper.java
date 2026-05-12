package com.phananh.e_commerce.presentation.mapper;

import com.phananh.e_commerce.presentation.dto.response.brand.BrandResponse;
import com.phananh.e_commerce.domain.model.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "brandId", source = "id")
    @Mapping(target = "brandName", source = "name")
    @Mapping(target = "brandImage", source = "imageUrl")
    BrandResponse toResponse(Brand brand);
}


