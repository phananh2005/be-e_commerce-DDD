package com.phananh.e_commerce.product.application.service;

import com.phananh.e_commerce.product.application.dto.response.customer.ProductSummaryResponse;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductDetailResponse;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import com.phananh.e_commerce.product.presentation.dto.request.product.customer.CustomerProductSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

import com.phananh.e_commerce.product.presentation.dto.request.product.staff.ProductCreateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.ProductUpdateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.VariantImageCreateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.VariantImageUpdateRequest;

public interface ProductService {

	// Customer - search active products
	Page<ProductSummaryResponse> getProductsActiveBySearch(CustomerProductSearchRequest customerProductSearchRequest);

	// Customer - product detail
	ProductDetailResponse getProductById(Long id);

	ProductVariant getProductVariantById(Long variantId);

	// Staff - search all products (including inactive)
	List<ProductSummaryResponse> getAllProductsBySearch(CustomerProductSearchRequest customerProductSearchRequest);

	// Staff - create/update operations
	void createProduct(ProductCreateRequest productCreateRequest);

	void createVariantImage(VariantImageCreateRequest variantImageCreateRequest);

	void updateProduct(ProductUpdateRequest productUpdateRequest);

	void updateVariantImage(VariantImageUpdateRequest variantImageUpdateRequest);

	void updateProductStatus(Long productId, String status);

	void updateVariantStock(Long variantId, Integer stockQuantity);
}


