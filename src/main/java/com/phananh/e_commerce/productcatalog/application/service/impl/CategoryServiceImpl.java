package com.phananh.e_commerce.productcatalog.application.service.impl;

import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.productcatalog.application.dto.query.CategorySearchQuery;
import com.phananh.e_commerce.productcatalog.domain.repository.CategoryRepository;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategorySearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryUpdateRequest;
import com.phananh.e_commerce.productcatalog.application.dto.response.CategoryResponse;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.productcatalog.application.mapper.CategoryMapper;
import com.phananh.e_commerce.productcatalog.domain.model.Category;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    CategoryRepository categoryRepository;
    CloudinaryService cloudinaryService;
    CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getEnabledCategories() {
        return categoryRepository.getEnabled()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategoriesBySearch(CategorySearchRequest request) {
        int page = PageUtils.getPageNumber(request.getPage());
        int size = PageUtils.getPageSize(request.getSize());
        String sortBy = PageUtils.getSortBy(request.getSortBy());
        String sortType = PageUtils.getSortType(request.getSortType());
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by(Sort.Direction.fromString(sortType), sortBy));

        CategorySearchQuery query = CategorySearchQuery.builder()
                .keyword(request.getKeyword() == null ? null : request.getKeyword().trim())
                .createdDateFrom(request.getCreatedDateFrom())
                .createdDateTo(request.getCreatedDateTo())
                .modifiedDateFrom(request.getModifiedDateFrom())
                .modifiedDateTo(request.getModifiedDateTo())
                .pageable(pageable)
                .build();

        return categoryRepository.getAllBySearch(query)
                .map(categoryMapper::toResponse);
    }

    @Override
    @Transactional
    public void createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getCategoryName().trim())) {
            throw new AppException(ErrorCode.CONFLICT);
        }

        Category category = Category.create(request.getCategoryName(), request.getCategoryDescription());
        category = categoryRepository.saveAndFlush(category);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String publicId = category.buildCategoryAvatarPublicId();
                String imageUrl = cloudinaryService.uploadFile(request.getImage(), "categories", publicId);
                category.updateImage(imageUrl);
            } catch (IOException e) {
                log.error("Error uploading category image", e);
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void updateCategory(CategoryUpdateRequest request) {
        Category category = categoryRepository.getById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Update category info (name and description)
        category.updateName(request.getCategoryName());
        category.updateDescription(request.getCategoryDescription());


        // Update category image
        String publicId = category.buildCategoryAvatarPublicId();

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadFile(request.getImage(), "categories", publicId);
                category.updateImage(imageUrl);
            } catch (IOException e) {
                log.error("Error uploading category image", e);
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        } else {
            try {
                cloudinaryService.deleteFile("categories/" + publicId);
                category.removeImage();
            } catch (IOException e) {
                log.error("Error deleting category image", e);
                throw new AppException(ErrorCode.FILE_DELETE_ERROR);
            }
        }

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void updateCategoryStatus(com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryStatusUpdateRequest request) {
        Category category = categoryRepository.getById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (request.getStatus()) category.active();
        else category.inactive();

        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public String getCategoryNameById(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.getById(categoryId)
                .map(Category::getName)
                .orElse(null);
    }
}


