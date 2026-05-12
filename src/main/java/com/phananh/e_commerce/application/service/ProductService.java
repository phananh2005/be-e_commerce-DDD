package com.phananh.e_commerce.application.service;

import com.phananh.e_commerce.presentation.dto.request.product.ProductCreateRequest;
import com.phananh.e_commerce.presentation.dto.request.product.ProductSearchRequest;
import com.phananh.e_commerce.presentation.dto.request.product.ProductUpdateRequest;
import com.phananh.e_commerce.presentation.dto.request.product.VariantImageCreateRequest;
import com.phananh.e_commerce.presentation.dto.request.product.VariantImageUpdateRequest;
import com.phananh.e_commerce.presentation.dto.response.product.ProductDetailResponse;
import com.phananh.e_commerce.presentation.dto.response.product.ProductSummaryResponse;
import com.phananh.e_commerce.domain.model.entity.ProductVariant;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

	// Customer - search active products
	Page<ProductSummaryResponse> getProductsActiveBySearch(ProductSearchRequest productSearchRequest);

	// Customer - product detail
	ProductDetailResponse getProductById(Long id);

	ProductVariant getProductVariantById(Long variantId);

	// Staff - search all products (including inactive)
	List<ProductSummaryResponse> getAllProductsBySearch(ProductSearchRequest productSearchRequest);

	// Staff - create/update operations
	void createProduct(ProductCreateRequest productCreateRequest);

	void createVariantImage(VariantImageCreateRequest variantImageCreateRequest);

	void updateProduct(ProductUpdateRequest productUpdateRequest);

	void updateVariantImage(VariantImageUpdateRequest variantImageUpdateRequest);

	void updateProductStatus(Long productId, String status);

	void updateVariantStock(Long variantId, Integer stockQuantity);
}



