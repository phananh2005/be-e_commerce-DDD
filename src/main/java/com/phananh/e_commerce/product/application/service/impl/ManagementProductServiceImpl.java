package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import com.phananh.e_commerce.core.util.ListUtils;
import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.product.application.dto.command.ProductCreateCommand;
import com.phananh.e_commerce.product.application.dto.command.ProductVariantCreateCommand;
import com.phananh.e_commerce.product.application.dto.query.ManagementProductSearchQuery;
import com.phananh.e_commerce.product.application.dto.response.management.ProductDetailResponseForManagement;
import com.phananh.e_commerce.product.application.dto.response.management.ProductSummaryResponseForManagement;
import com.phananh.e_commerce.product.application.dto.response.management.ProductVariantResponseForManagement;
import com.phananh.e_commerce.product.application.dto.response.management.ProductVariantsSummaryResponseForManagement;
import com.phananh.e_commerce.product.application.mapper.ManagementProductMapper;
import com.phananh.e_commerce.product.application.service.ManagementProductService;
import com.phananh.e_commerce.product.domain.model.*;
import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import com.phananh.e_commerce.product.presentation.dto.request.management.ManagementProductSearchRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductCreateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductUpdateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductVariantCreateRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ManagementProductServiceImpl implements ManagementProductService {

    ProductRepository productRepository;
    ManagementProductMapper managementProductMapper;
    CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummaryResponseForManagement> getAllProductsBySearch(ManagementProductSearchRequest managementProductSearchRequest) {
        int page = PageUtils.getPageNumber(managementProductSearchRequest.getPage());
        int size = PageUtils.getPageSize(managementProductSearchRequest.getSize());
        String sortBy = PageUtils.getSortBy(managementProductSearchRequest.getSortBy());
        String sortType = PageUtils.getSortType(managementProductSearchRequest.getSortType());
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortType), sortBy));

        ManagementProductSearchQuery query = ManagementProductSearchQuery.builder()
                .productSearch(managementProductSearchRequest.getProductSearch() == null || managementProductSearchRequest.getProductSearch().isBlank() ? null : managementProductSearchRequest.getProductSearch().trim())
                .categoryIds(managementProductSearchRequest.getCategoryIds())
                .brandIds(managementProductSearchRequest.getBrandIds())
                .status(managementProductSearchRequest.getStatus())
                .pageable(pageable)
                .build();

        return productRepository.getAllProductsBySearch(query)
                .map(managementProductMapper::toManagementProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponseForManagement getManagementProductById(Long id) {
        Product product = productRepository.getProductById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return managementProductMapper.toManagementProductDetailResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponseForManagement> getManagementProductVariantsByProductId(Long productId) {
        if (productRepository.getProductById(productId).isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productRepository.getVariantsByProductId(productId).stream()
                .map(managementProductMapper::toManagementProductVariantResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantsSummaryResponseForManagement getManagementProductVariantsSummaryByProductId(Long productId) {
        Product product = productRepository.getProductById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<ProductVariantsSummaryResponseForManagement.Variant> variants = productRepository.getVariantsByProductId(productId).stream()
                .map(managementProductMapper::toManagementProductVariantSummary)
                .toList();

        ProductVariantsSummaryResponseForManagement response = new ProductVariantsSummaryResponseForManagement();
        response.setProductId(product.getId());
        response.setVariants(variants);
        return response;
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

        if (!ListUtils.isNullOrEmpty(request.getVariants())) {
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
                if (!ListUtils.isNullOrEmpty(variantRequest.getVariantImageUrls())) {
                    for (String imageUrl : variantRequest.getVariantImageUrls()) {
                        if (!StringUtils.isBlank(imageUrl)) {
                            images.add(VariantImage.create(variant, imageUrl, false));
                        }
                    }
                }

                // Process attributes if provided
                if (variantRequest.getAttributes() != null && !variantRequest.getAttributes().isEmpty()) {
                    attributeValues.addAll(getAttributeValues(variantRequest.getAttributes()));
                }

                variants.add(variant);
            }
        }

        productRepository.save(product);
    }

    private Set<AttributeValue> getAttributeValues(Map<String,String> attributesValues) {
        Set<AttributeValue> attributeValues = new HashSet<>();
        for (Map.Entry<String, String> attribute : attributesValues.entrySet()) {
            String name = attribute.getKey();
            String value = attribute.getValue();

            Optional<ProductAttribute> productAttributeOpt = productRepository.getProductAttributesByName(name);
            AttributeValue attributeValue;
            if (productAttributeOpt.isPresent()) {
                ProductAttribute productAttribute = productAttributeOpt.get();
                Set<AttributeValue> existingValues = productAttribute.getAttributeValues();
                attributeValue = existingValues.stream()
                        .filter(v -> v.getValue().equals(value))
                        .findFirst()
                        .orElseGet(() -> {
                            AttributeValue newValue = AttributeValue.create(value, productAttribute);
                            productRepository.save(newValue);
                            return newValue;
                        });
            }
            else {
                // Handle the case where the attribute doesn't exist
                ProductAttribute productAttribute = ProductAttribute.builder()
                        .name(name)
                        .build();

                attributeValue = AttributeValue.create(value, productAttribute);
                productRepository.save(attributeValue);
            }

            attributeValues.add(attributeValue);
        }
        return attributeValues;
    }

    @Override
    @Transactional
    public void updateProduct(ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.getProductById(productUpdateRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.updateName(productUpdateRequest.getName());
        product.updateDescription(productUpdateRequest.getDescription());
        product.updateCategoryId(productUpdateRequest.getCategoryId());
        product.updateBrandId(productUpdateRequest.getBrandId());

        // Update image handling using imageUrl semantics:
        // - imageUrl == null => keep existing image
        // - imageUrl is non-empty => set this new URL
        // - imageUrl is empty string ("") => remove existing image
        if (productUpdateRequest.getProductAvatarUrl() != null) {
            product.updateAvatarUrl(productUpdateRequest.getProductAvatarUrl());
        }

        // Update variants
        if (!ListUtils.isNullOrEmpty(productUpdateRequest.getVariants())) {
            for (ProductUpdateRequest.VariantUpdateRequest variantRequest : productUpdateRequest.getVariants()) {
                try {
                    ProductVariant variant = productRepository.getVariantById(variantRequest.getVariantId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

                    // Update variant basic info
                    variant.updateSkuCode(variantRequest.getSkuCode());
                    variant.updatePrice(variantRequest.getPrice());
                    variant.updateStockQuantity(variantRequest.getStockQuantity());

                    // Handle avatar URL update
                    // - imageUrl == null => keep existing image
                    // - imageUrl is non-empty => set this new URL
                    // - imageUrl is empty string ("") => remove existing image
                    if (variantRequest.getVariantAvatarUrl() != null) {
                        if (variantRequest.getVariantAvatarUrl().isBlank()) variant.removeAvatar();
                        else
                            variant.updateAvatar(VariantImage.create(variant, variantRequest.getVariantAvatarUrl(), true));
                    }

                    // Delete images by ID
                    if (!ListUtils.isNullOrEmpty(variantRequest.getVariantImageIdsToDelete())) {
                        List<VariantImage> variantImages = productRepository
                                .getVariantImagesById(variantRequest.getVariantImageIdsToDelete());
                        variant.removeListImages(variantImages);

                        List<String> imageUrls = variantImages.stream().map(VariantImage::getImageUrl).toList();
                        for (String url : imageUrls) cloudinaryService.deleteFileByUrl(url);
                    }

                    // Add new gallery images
                    if (!ListUtils.isNullOrEmpty(variantRequest.getVariantImagesUrlsToAdd())) {
                        variant.addListImage(variantRequest.getVariantImagesUrlsToAdd().stream()
                                .filter(url -> !StringUtils.isBlank(url))
                                .map(url -> VariantImage.create(variant, url, false))
                                .toList());
                    }

                    // Update variant attributes
                    if (variantRequest.getAttributes() != null && !variantRequest.getAttributes().isEmpty()) {
                        variant.updateAttributeValues(getAttributeValues(variantRequest.getAttributes()));
                    }

                } catch (ObjectOptimisticLockingFailureException e) {
                    throw new AppException(ErrorCode.CONCURRENT_UPDATE_ERROR);
                }
            }
        }

        if (!ListUtils.isNullOrEmpty(productUpdateRequest.getNewVariants())) {
            for (ProductUpdateRequest.VariantCreateRequest newVariantRequest : productUpdateRequest.getNewVariants()) {
                Set<VariantImage> images = new HashSet<>();
                Set<AttributeValue> attributeValues = new HashSet<>();

                ProductVariantCreateCommand variantCreateCommand = ProductVariantCreateCommand.builder()
                        .product(product)
                        .skuCode(newVariantRequest.getSkuCode())
                        .price(newVariantRequest.getPrice())
                        .stockQuantity(newVariantRequest.getStockQuantity())
                        .images(images)
                        .attributeValues(attributeValues)
                        .build();

                ProductVariant variant = ProductVariant.create(variantCreateCommand);

                if (!StringUtils.isBlank(newVariantRequest.getVariantAvatarUrl())) {
                    images.add(VariantImage.create(variant, newVariantRequest.getVariantAvatarUrl(), true));
                }

                if (!ListUtils.isNullOrEmpty(newVariantRequest.getVariantImageUrls())) {
                    for (String imageUrl : newVariantRequest.getVariantImageUrls()) {
                        if (!StringUtils.isBlank(imageUrl)) {
                            images.add(VariantImage.create(variant, imageUrl, false));
                        }
                    }
                }

                if (newVariantRequest.getAttributes() != null && !newVariantRequest.getAttributes().isEmpty()) {
                    attributeValues.addAll(getAttributeValues(newVariantRequest.getAttributes()));
                }

                product.addVariant(variant);
            }
        }

        productRepository.save(product);
    }

    @Override
    public void updateProductStatus(Long productId, String status) {
        Product product = productRepository.getProductById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (StringUtils.isBlank(status)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        ProductStatus productStatus;
        try {
            productStatus = ProductStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        switch (productStatus) {
            case ACTIVE -> product.activate();
            case INACTIVE -> product.inactivate();
            case DRAFT -> product.isDraft();
        }

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateVariantStockQuantityAndPrice(Long variantId, com.phananh.e_commerce.product.presentation.dto.request.management.UpdateVariantStockQuantityAndPriceRequest request) {
        ProductVariant variant = productRepository.getVariantById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        variant.updateStockQuantity(request.getStockQuantity());
        variant.updatePrice(request.getPrice());
        productRepository.save(variant);
    }

    @Override
    @Transactional
    public void updateVariantStock(Long variantId, Integer stockQuantity) {
        ProductVariant variant = productRepository.getVariantById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        variant.updateStockQuantity(stockQuantity);
        productRepository.save(variant);
    }

}


