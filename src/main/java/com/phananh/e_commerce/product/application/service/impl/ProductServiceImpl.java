package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.product.application.dto.query.ProductSearchQuery;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductSummaryResponse;
import com.phananh.e_commerce.product.application.mapper.ProductMapper;
import com.phananh.e_commerce.product.application.service.ProductService;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import com.phananh.e_commerce.product.presentation.dto.request.product.customer.CustomerProductSearchRequest;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.ProductCreateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.ProductUpdateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.VariantImageCreateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.VariantImageUpdateRequest;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductDetailResponse;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataProductVariantRepository;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;
    BrandService brandService;
    CategoryService categoryService;
    SpringDataProductVariantRepository productVariantRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummaryResponse> getProductsActiveBySearch(CustomerProductSearchRequest customerProductSearchRequest) {
        int page = resolvePage(customerProductSearchRequest.getPage());
        Pageable pageable = PageRequest.of(
                page - 1,
                customerProductSearchRequest.getSize(),
                Sort.by(Sort.Direction.fromString(customerProductSearchRequest.getSortType()), customerProductSearchRequest.getSortBy())
        );

        ProductSearchQuery query = ProductSearchQuery.builder()
                .keyword(customerProductSearchRequest.getKeyword() == null ? null : customerProductSearchRequest.getKeyword().trim())
                .categoryId(customerProductSearchRequest.getCategoryId())
                .brandId(customerProductSearchRequest.getBrandId())
                .minPrice(customerProductSearchRequest.getMinPrice())
                .maxPrice(customerProductSearchRequest.getMaxPrice())
                .pageable(pageable)
                .build();

        Page<Product> products = productRepository.getProductsActiveBySearch(query);
        return products.map(productMapper::toProductSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductById(Long id) {
        Product product = productRepository.getById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Kiểm tra product có variant không
        if (product.getVariants() == null || product.getVariants().isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
        }

        // Map product sang ProductDetailResponse
        ProductDetailResponse response = productMapper.toProductDetailResponse(product);

        // Lấy brand name và category name từ BrandService và CategoryService
        if (product.getBrandId() != null) {
            response.setBrandName(brandService.getBrandNameById(product.getBrandId()));
        }
        if (product.getCategoryId() != null) {
            response.setCategoryName(categoryService.getCategoryNameById(product.getCategoryId()));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSummaryResponse> getAllProductsBySearch(CustomerProductSearchRequest customerProductSearchRequest) {
        int page = resolvePage(customerProductSearchRequest.getPage());
        Pageable pageable = PageRequest.of(
                page - 1,
                customerProductSearchRequest.getSize(),
                Sort.by(Sort.Direction.fromString(customerProductSearchRequest.getSortType()), customerProductSearchRequest.getSortBy())
        );

        ProductSearchQuery query = ProductSearchQuery.builder()
                .keyword(customerProductSearchRequest.getKeyword() == null ? null : customerProductSearchRequest.getKeyword().trim())
                .categoryId(customerProductSearchRequest.getCategoryId())
                .brandId(customerProductSearchRequest.getBrandId())
                .minPrice(customerProductSearchRequest.getMinPrice())
                .maxPrice(customerProductSearchRequest.getMaxPrice())
                .pageable(pageable)
                .build();

        List<Product> products = productRepository.getAllProductsBySearch(query);
        return products.stream().map(productMapper::toProductSummaryResponse).toList();
    }

    private int resolvePage(Integer page) {
        return page == null || page < 1 ? 1 : page;
    }

    @Override
    public void createProduct(ProductCreateRequest productCreateRequest) {
        // Implementation placeholder for product creation
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

    @Override
    public ProductVariant getProductVariantById(Long variantId) {
        return productVariantRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }
}


