package com.phananh.e_commerce.productcatalog.application.mapper;

import com.phananh.e_commerce.productcatalog.presentation.dto.response.brand.BrandResponse;
import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "brandId", source = "id")
    @Mapping(target = "brandName", source = "name")
    @Mapping(target = "brandDescription", source = "description")
    @Mapping(target = "brandImage", source = "imageUrl")
    BrandResponse toResponse(Brand brand);
}


