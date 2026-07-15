package com.phananh.e_commerce.product.application.service;

import com.phananh.e_commerce.product.application.dto.response.staff.ProductResponse;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductVariantResponse;
import com.phananh.e_commerce.product.presentation.dto.request.product.management.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ManagementProductService {


	// Management - product detail
	ProductResponse getManagementProductById(Long id);

	// Management - list variants by product
	List<ProductVariantResponse> getManagementProductVariantsByProductId(Long productId);

	// Management - search all products (including inactive)
	Page<ProductResponse> getAllProductsBySearch(ManagementProductSearchRequest managementProductSearchRequest);

	// Management - create/update operations
	void createProduct(ProductCreateRequest productCreateRequest);

	void createProductVariant(Long productId, ProductVariantCreateRequest productVariantCreateRequest);

	void updateProduct(ProductUpdateRequest productUpdateRequest);

	void updateProductStatus(Long productId, String status);

	void updateVariantStock(Long variantId, Integer stockQuantity);
}


