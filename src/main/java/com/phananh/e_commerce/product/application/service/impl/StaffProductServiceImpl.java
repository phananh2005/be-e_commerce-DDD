package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.product.application.dto.command.ProductCreateCommand;
import com.phananh.e_commerce.product.application.dto.command.ProductVariantCreateCommand;
import com.phananh.e_commerce.product.application.dto.query.StaffProductSearchQuery;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductResponse;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductVariantResponse;
import com.phananh.e_commerce.product.application.mapper.StaffProductMapper;
import com.phananh.e_commerce.product.application.service.StaffProductService;
import com.phananh.e_commerce.product.domain.model.*;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.*;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StaffProductServiceImpl implements StaffProductService {

    ProductRepository productRepository;
    StaffProductMapper staffProductMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProductsBySearch(StaffProductSearchRequest request) {
        int page = PageUtils.getPageNumber(request.getPage());
        int size = PageUtils.getPageSize(request.getSize());
        String sortBy = PageUtils.getSortBy(request.getSortBy());
        String sortType = PageUtils.getSortType(request.getSortType());
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by(Sort.Direction.fromString(sortType), sortBy));

        StaffProductSearchQuery query = StaffProductSearchQuery.builder()
                .keyword(request.getKeyword() == null || request.getKeyword().isBlank() ? null : request.getKeyword().trim())
                .categoryId(request.getCategoryId())
                .brandId(request.getBrandId())
                .minPrice(request.getMinPrice())
                .maxPrice(request.getMaxPrice())
                .minRating(request.getMinRating())
                .pageable(pageable)
                .build();

        return productRepository.getAllProductsBySearch(query)
                .map(staffProductMapper::toStaffProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getStaffProductById(Long id) {
        Product product = productRepository.getById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return staffProductMapper.toStaffProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getStaffProductVariantsByProductId(Long productId) {
        if (productRepository.getById(productId).isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productRepository.getVariantsByProductId(productId).stream()
                .map(staffProductMapper::toStaffProductVariantResponse)
                .toList();
    }

    @Override
    @Transactional
    public void createProduct(ProductCreateRequest request) {
        Set<ProductVariant> variants = new HashSet<>();

        ProductCreateCommand productCreateCommand = ProductCreateCommand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .avatarUrl(request.getProductAvatarUrl())
                .categoryId(request.getCategoryId())
                .brandId(request.getBrandId())
                .variants(variants)
                .build();

        Product product = Product.create(productCreateCommand);

        for (ProductCreateRequest.VariantCreateRequest variantRequest : request.getVariants()) {
            Set<VariantImage> images = new HashSet<>();
            Set<AttributeValue> attributeValues = new HashSet<>();
            ProductVariantCreateCommand variantCreateCommand = ProductVariantCreateCommand.builder()
                    .product(product)
                    .skuCode(variantRequest.getSkuCode())
                    .price(variantRequest.getPrice())
                    .stockQuantity(variantRequest.getStockQuantity())
                    .images(images)
                    .attributeValues(attributeValues)
                    .build();
            ProductVariant variant = ProductVariant.create(variantCreateCommand);

            // Add variant avatar (primary image) if URL is provided
            if (!StringUtils.isBlank(variantRequest.getVariantAvatarUrl())) {
                images.add(VariantImage.create(variant, variantRequest.getVariantAvatarUrl(), true));
            }

            // Add variant gallery images if URLs are provided
            if (variantRequest.getVariantImageUrls() != null && !variantRequest.getVariantImageUrls().isEmpty()) {
                for (String imageUrl : variantRequest.getVariantImageUrls()) {
                    if (!StringUtils.isBlank(imageUrl)) {
                        images.add(VariantImage.create(variant, imageUrl, false));
                    }
                }
            }

            // Process attributes if provided
            if (variantRequest.getAttributes() != null && !variantRequest.getAttributes().isEmpty()) {
                for(Map.Entry<String, String> attribute : variantRequest.getAttributes().entrySet()) {
                String name = attribute.getKey();
                String value = attribute.getValue();

                ProductAttribute productAttribute = productRepository.getProductAttributesByName(name)
                        .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
                Set<AttributeValue> existingValues = productAttribute.getAttributeValues();
                AttributeValue attributeValue = existingValues.stream()
                        .filter(v -> v.getValue().equals(value))
                        .findFirst()
                        .orElseGet(() -> {
                            AttributeValue newValue = AttributeValue.create(value, productAttribute);
                            productRepository.save(newValue);
                            return newValue;
                        });

                attributeValues.add(attributeValue);
                }
            }

            variants.add(variant);
        }

        productRepository.save(product);
    }


    @Override
    @Transactional
    public void updateProduct(ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.getById(productUpdateRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Update product basic info
        if (productUpdateRequest.getName() != null && !productUpdateRequest.getName().isBlank()) {
            product.updateName(productUpdateRequest.getName());
        }
        if (productUpdateRequest.getDescription() != null) {
            product.updateDescription(productUpdateRequest.getDescription());
        }
        if (productUpdateRequest.getCategoryId() != null) {
            product.updateCategoryId(productUpdateRequest.getCategoryId());
        }
        if (productUpdateRequest.getBrandId() != null) {
            product.updateBrandId(productUpdateRequest.getBrandId());
        }
        if (productUpdateRequest.getProductAvatarUrl() != null && !productUpdateRequest.getProductAvatarUrl().isBlank()) {
            product.updateAvatarUrl(productUpdateRequest.getProductAvatarUrl());
        }

        // Update variants
        if (productUpdateRequest.getVariants() != null && !productUpdateRequest.getVariants().isEmpty()) {
            for (ProductUpdateRequest.VariantUpdateRequest variantRequest : productUpdateRequest.getVariants()) {
                ProductVariant variant = productRepository.getVariantBySkuCode(variantRequest.getSkuCode())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

                // Update variant basic info
                if (variantRequest.getPrice() != null) {
                    variant.updatePrice(variantRequest.getPrice());
                }
                if (variantRequest.getStockQuantity() != null) {
                    variant.updateStockQuantity(variantRequest.getStockQuantity());
                }

                // Update variant attributes
                if (variantRequest.getAttributes() != null && !variantRequest.getAttributes().isEmpty()) {
                    Set<AttributeValue> attributeValues = new HashSet<>();
                    for (Map.Entry<String, String> attribute : variantRequest.getAttributes().entrySet()) {
                        String attrName = attribute.getKey();
                        String attrValue = attribute.getValue();

                        ProductAttribute productAttribute = productRepository.getProductAttributesByName(attrName)
                                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
                        Set<AttributeValue> existingValues = productAttribute.getAttributeValues();
                        AttributeValue attributeValue = existingValues.stream()
                                .filter(v -> v.getValue().equals(attrValue))
                                .findFirst()
                                .orElseGet(() -> {
                                    AttributeValue newValue = AttributeValue.create(attrValue, productAttribute);
                                    productRepository.save(newValue);
                                    return newValue;
                                });
                        attributeValues.add(attributeValue);
                    }
                    variant.updateAttributeValues(attributeValues);
                }

                // Update variant images
                Set<VariantImage> images = new HashSet<>(variant.getImages());

                // Delete images by ID
                if (variantRequest.getVariantImageIdsToDelete() != null && !variantRequest.getVariantImageIdsToDelete().isEmpty()) {
                    images.removeIf(img -> variantRequest.getVariantImageIdsToDelete().contains(img.getId()));
                }

                // Handle avatar URL update
                if (variantRequest.getVariantAvatarUrl() != null && !variantRequest.getVariantAvatarUrl().isBlank()) {
                    // Remove old avatar if exists
                    images.stream()
                            .filter(VariantImage::isAvatar)
                            .findFirst()
                            .ifPresent(images::remove);
                    // Add new avatar
                    images.add(VariantImage.create(variant, variantRequest.getVariantAvatarUrl(), true));
                }

                // Add new gallery images
                if (variantRequest.getVariantImagesUrlsToAdd() != null && !variantRequest.getVariantImagesUrlsToAdd().isEmpty()) {
                    for (String imageUrl : variantRequest.getVariantImagesUrlsToAdd()) {
                        if (imageUrl != null && !imageUrl.isBlank()) {
                            images.add(VariantImage.create(variant, imageUrl, false));
                        }
                    }
                }

                variant.updateImages(images);
                productRepository.save(variant);
            }
        }

        productRepository.save(product);
    }

    @Override
    public void updateVariantImage(VariantImageUpdateRequest variantImageUpdateRequest) {
        // Implementation placeholder for variant image update
    }

    @Override
    public void updateProductStatus(Long productId, String status) {
        // Implementation placeholder for product status update
    }

    @Override
    public void updateVariantStock(Long variantId, Integer stockQuantity) {
        // Implementation placeholder for variant stock update
    }


}


