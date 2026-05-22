package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import com.phananh.e_commerce.core.util.FileUtils;
import com.phananh.e_commerce.core.util.PageUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    CloudinaryService cloudinaryService;

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

    private String uploadVariantImage(Product product, MultipartFile file, String skuCode, String suffix) {
        try {
            String publicId = "product-" + product.getId() + "-variant-" + skuCode.trim() + "-" + suffix;
            return cloudinaryService.uploadFile(file, "products/variants", publicId);
        } catch (IOException e) {
            log.error("Error uploading variant image for productId={}, skuCode={}", product.getId(), skuCode, e);
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    @Transactional
    public void createProduct(ProductCreateRequest request) {
        ProductCreateCommand productCreateCommand = ProductCreateCommand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .categoryId(request.getCategoryId())
                .brandId(request.getBrandId())
                .build();

        Product product = Product.create(productCreateCommand);

        product = productRepository.saveAndFlush(product);

        if(!FileUtils.isEmpty(request.getProductAvatar())){
            try {
                String avatarUrl = cloudinaryService.uploadFile(request.getProductAvatar(), "products", product.buildProductAvatarPublicId());
                product.updateAvatarUrl(avatarUrl);
            } catch (IOException e) {
                log.error("Error uploading product image", e);
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        Set<ProductVariant> variants = new HashSet<>();
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

            if (FileUtils.isEmpty(variantRequest.getVariantAvatar())) {
                String variantAvatarUrl = uploadVariantImage(product, variantRequest.getVariantAvatar(), variantRequest.getSkuCode(), "avatar");
                images.add(VariantImage.create(variant, variantAvatarUrl, true));
            }

            if (FileUtils.isEmpty(variantRequest.getVariantImages())) {
                int imageIndex = 0;
                for (MultipartFile imageFile : variantRequest.getVariantImages()) {
                    if (imageFile == null || imageFile.isEmpty()) {
                        continue;
                    }
                    imageIndex++;
                    String imageUrl = uploadVariantImage(product, imageFile, variantRequest.getSkuCode(), "image-" + imageIndex);
                    images.add(VariantImage.create(variant, imageUrl, false));
                }
            }

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

            variants.add(variant);
        }

        product.updateVariants(variants);
        productRepository.save(product);
    }


    @Override
    public void updateProduct(ProductUpdateRequest productUpdateRequest) {
        // Implementation placeholder for product update
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


