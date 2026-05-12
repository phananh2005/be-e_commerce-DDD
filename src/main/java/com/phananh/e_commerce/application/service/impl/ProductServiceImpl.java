package com.phananh.e_commerce.application.service.impl;

import com.phananh.e_commerce.presentation.dto.response.product.ProductDetailResponse;
import com.phananh.e_commerce.presentation.dto.response.product.ProductSummaryResponse;
import com.phananh.e_commerce.application.exception.AppException;
import com.phananh.e_commerce.application.exception.ErrorCode;
import com.phananh.e_commerce.presentation.mapper.ProductMapper;
import com.phananh.e_commerce.domain.model.entity.Product;
import com.phananh.e_commerce.domain.model.entity.ProductVariant;
import com.phananh.e_commerce.infrastructure.persistence.repository.ProductRepository;
import com.phananh.e_commerce.infrastructure.persistence.repository.ProductVariantRepository;
import com.phananh.e_commerce.infrastructure.persistence.repository.specification.ProductSearchSpecification;
import com.phananh.e_commerce.application.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    ProductVariantRepository productVariantRepository;
    ProductMapper productMapper;

    @Override
    @Transactional()
    public Page<ProductSummaryResponse> getProductsActiveBySearch(ProductSearchRequest productSearchRequest) {
        Specification<Product> specification = Specification.where(ProductSearchSpecification.hasNameLike(productSearchRequest.getKeyword()))
                .and(ProductSearchSpecification.hasCategoryId(productSearchRequest.getCategoryId()))
                .and(ProductSearchSpecification.hasBrandId(productSearchRequest.getBrandId()))
                .and(ProductSearchSpecification.hasPriceBetween(productSearchRequest.getMinPrice(), productSearchRequest.getMaxPrice()))
                .and(ProductSearchSpecification.hasStatus("ACTIVE"));

        int page = productSearchRequest.getPage();
        int size = productSearchRequest.getSize();
        Sort sort = Sort.by(Sort.Direction.fromString(productSearchRequest.getSortDirection()), productSearchRequest.getSortBy());

        Page<Product> products = productRepository.findAll(specification, PageRequest.of(page, size, sort));
        return products.map(productMapper::toProductSummaryResponse);
    }

    @Override
    public ProductDetailResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        List<ProductVariant> variants = productVariantRepository.findByProduct_Id(product.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));


        return null;
    }

    @Override
    public List<ProductSummaryResponse> getAllProductsBySearch(ProductSearchRequest productSearchRequest) {
        return List.of();
    }

    @Override
    public void createProduct(ProductCreateRequest productCreateRequest) {

    }

    @Override
    public void createVariantImage(VariantImageCreateRequest variantImageCreateRequest) {

    }

    @Override
    public void updateProduct(ProductUpdateRequest productUpdateRequest) {

    }

    @Override
    public void updateVariantImage(VariantImageUpdateRequest variantImageUpdateRequest) {

    }

    @Override
    public void updateProductStatus(Long productId, String status) {

    }

    @Override
    public void updateVariantStock(Long variantId, Integer stockQuantity) {

    }

    //
    @Override
    public ProductVariant getProductVariantById(Long variantId) {
        return null;
    }
}



