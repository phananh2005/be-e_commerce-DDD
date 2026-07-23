package com.phananh.e_commerce.product.application.service;

import com.phananh.e_commerce.product.application.dto.response.management.ProductDetailResponseForManagement;
import com.phananh.e_commerce.product.application.dto.response.management.ProductSummaryResponseForManagement;
import com.phananh.e_commerce.product.application.dto.response.management.ProductVariantResponseForManagement;
import com.phananh.e_commerce.product.presentation.dto.request.management.ManagementProductSearchRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductCreateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductUpdateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductVariantCreateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ManagementProductService {


	// Management - product detail
	ProductDetailResponseForManagement getManagementProductById(Long id);

	// Management - list variants by product
	List<ProductVariantResponseForManagement> getManagementProductVariantsByProductId(Long productId);

	// Management - search all products (including inactive)
	Page<ProductSummaryResponseForManagement> getAllProductsBySearch(ManagementProductSearchRequest managementProductSearchRequest);

	// Management - create/update operations
	void createProduct(ProductCreateRequest productCreateRequest);

	void createProductVariant(Long productId, ProductVariantCreateRequest productVariantCreateRequest);

	void updateProduct(ProductUpdateRequest productUpdateRequest);

	void updateProductStatus(Long productId, String status);

	void updateVariantStock(Long variantId, Integer stockQuantity);
}


