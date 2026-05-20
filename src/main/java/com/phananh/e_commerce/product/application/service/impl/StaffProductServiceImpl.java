package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.product.application.dto.query.StaffProductSearchQuery;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductResponse;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductVariantResponse;
import com.phananh.e_commerce.product.application.mapper.StaffProductMapper;
import com.phananh.e_commerce.product.application.service.StaffProductService;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import com.phananh.e_commerce.product.domain.model.VariantImage;
import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.*;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
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
import java.util.HashMap;
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
    BrandService brandService;
    CategoryService categoryService;
    CloudinaryService cloudinaryService;

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
    @Transactional
    public void createProduct(ProductCreateRequest productCreateRequest) {
        validateProductCatalog(productCreateRequest.getCategoryId(), productCreateRequest.getBrandId());

        if (productCreateRequest.getProductAvatar() == null || productCreateRequest.getProductAvatar().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if (productCreateRequest.getVariants() == null || productCreateRequest.getVariants().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        validateVariantSku(productCreateRequest.getVariants());

        Product product = Product.create(
                productCreateRequest.getName(),
                productCreateRequest.getDescription(),
                productCreateRequest.getCategoryId(),
                productCreateRequest.getBrandId(),
                "temp-avatar",
                ProductStatus.DRAFT
        );

        product = productRepository.saveAndFlush(product);

        String avatarUrl = uploadProductAvatar(product, productCreateRequest.getProductAvatar());
        product.updateAvatarUrl(avatarUrl);

        Set<ProductVariant> variants = new HashSet<>();
        for (ProductCreateRequest.VariantCreateRequest variantRequest : productCreateRequest.getVariants()) {
            Set<VariantImage> images = new HashSet<>();
            ProductVariant variant = ProductVariant.create(
                    product,
                    variantRequest.getSkuCode(),
                    variantRequest.getPrice(),
                    variantRequest.getStockQuantity(),
                    images,
                    null
            );

            if (variantRequest.getVariantAvatar() != null && !variantRequest.getVariantAvatar().isEmpty()) {
                String variantAvatarUrl = uploadVariantImage(product, variantRequest.getVariantAvatar(), variantRequest.getSkuCode(), "avatar");
                images.add(VariantImage.create(variant, variantAvatarUrl, true));
            }

            if (variantRequest.getVariantImages() != null) {
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

            variants.add(variant);
        }

        product.replaceVariants(variants);
        productRepository.save(product);
    }

    private void validateVariantSku(List<ProductCreateRequest.VariantCreateRequest> variants) {
        Map<String, Integer> skuCounter = new HashMap<>();
        for (ProductCreateRequest.VariantCreateRequest variant : variants) {
            String sku = variant.getSkuCode() == null ? "" : variant.getSkuCode().trim();
            if (sku.isBlank()) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
            int count = skuCounter.getOrDefault(sku.toLowerCase(), 0) + 1;
            skuCounter.put(sku.toLowerCase(), count);
            if (count > 1) {
                throw new AppException(ErrorCode.CONFLICT);
            }
        }
    }

    private void validateProductCatalog(Long categoryId, Long brandId) {
        if (categoryService.getCategoryNameById(categoryId) == null) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        if (brandService.getBrandNameById(brandId) == null) {
            throw new AppException(ErrorCode.BRAND_NOT_FOUND);
        }
    }

    private String uploadProductAvatar(Product product, MultipartFile productAvatar) {
        try {
            return cloudinaryService.uploadFile(productAvatar, "products", product.buildProductAvatarPublicId());
        } catch (IOException e) {
            log.error("Error uploading product avatar for productId={}", product.getId(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
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
    public void createVariantImage(VariantImageCreateRequest variantImageCreateRequest) {
        // Implementation placeholder for variant image creation
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


