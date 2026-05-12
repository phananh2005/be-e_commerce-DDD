package com.phananh.e_commerce.presentation.mapper;

import com.phananh.e_commerce.presentation.dto.response.category.CategoryResponse;
import com.phananh.e_commerce.domain.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "categoryId", source = "id")
    @Mapping(target = "categoryName", source = "name")
    @Mapping(target = "categoryDescription", source = "description")
    @Mapping(target = "imageUrl", source = "imageUrl")
    CategoryResponse toResponse(Category category);
}

