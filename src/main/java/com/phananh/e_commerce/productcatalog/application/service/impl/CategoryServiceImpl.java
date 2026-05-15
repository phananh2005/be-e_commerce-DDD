package com.phananh.e_commerce.productcatalog.application.service.impl;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategorySearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.category.CategoryResponse;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.productcatalog.application.mapper.CategoryMapper;
import com.phananh.e_commerce.productcatalog.domain.model.Category;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata.SpringDataCategoryRepository;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    SpringDataCategoryRepository springDataCategoryRepository;
    CloudinaryService cloudinaryService;
    CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(CategorySearchRequest categorySearchRequest) {
        List<Category> categories = springDataCategoryRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(categorySearchRequest.getKeyword(),
                        categorySearchRequest.getKeyword(),
                        PageRequest.of(categorySearchRequest.getPage(), categorySearchRequest.getSize(), Sort.by(Sort.Direction.ASC, "name")))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categories.stream().map(categoryMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        String normalizedName = normalizeName(request.getCategoryName());
        if (springDataCategoryRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new AppException(ErrorCode.CONFLICT);
        }

        Category category = Category.builder()
                .name(request.getCategoryName())
                .description(request.getCategoryDescription())
                .build();
        category = springDataCategoryRepository.saveAndFlush(category);
        if(request.getImage() != null && !request.getImage().isEmpty()){
            try {
                String publicId = buildCategoryAvatarPublicId(category.getId());
                String imageUrl = cloudinaryService.uploadFile(request.getImage(), "categories", publicId);
                category.setImageUrl(imageUrl);
            } catch (IOException e) {
                log.error("Error uploading category image", e);
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(CategoryUpdateRequest request) {
        Category category = springDataCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        String normalizedName = normalizeName(request.getCategoryName());
        category.setName(normalizedName);
        category.setDescription(request.getCategoryDescription());

        if(request.getImage() != null && !request.getImage().isEmpty()){
            try {
                String publicId = buildCategoryAvatarPublicId(category.getId());
                String imageUrl = cloudinaryService.uploadFile(request.getImage(), "categories", publicId);
                category.setImageUrl(imageUrl);
            } catch (IOException e) {
                log.error("Error uploading category image", e);
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        category = springDataCategoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    private String normalizeName(String name) {
        String normalizedName = name == null ? "" : name.trim();
        if (normalizedName.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        return normalizedName;
    }

    private String buildCategoryAvatarPublicId(Long categoryId) {
        return "category-" + categoryId + "-avatar";
    }
}


