package com.phananh.e_commerce.product.application.service;

import com.phananh.e_commerce.product.application.dto.response.management.ProductDetailResponseForManagement;
import com.phananh.e_commerce.product.application.dto.response.management.ProductSummaryResponseForManagement;
import com.phananh.e_commerce.product.application.dto.response.management.ProductVariantResponseForManagement;
import com.phananh.e_commerce.product.application.dto.response.management.ProductVariantsSummaryResponseForManagement;
import com.phananh.e_commerce.product.presentation.dto.request.management.ManagementProductSearchRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductCreateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductUpdateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductVariantCreateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.UpdateVariantStockQuantityAndPriceRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ManagementProductService {


	// Management - product detail
	ProductDetailResponseForManagement getManagementProductById(String uuid);

	// Management - list variants by product
	List<ProductVariantResponseForManagement> getManagementProductVariantsByProductId(String uuid);

	// Management - get variants summary by product
	ProductVariantsSummaryResponseForManagement getManagementProductVariantsSummaryByProductId(String uuid);

	// Management - search all products (including inactive)
	Page<ProductSummaryResponseForManagement> getAllProductsBySearch(ManagementProductSearchRequest managementProductSearchRequest);

	// Management - create/update operations
	void createProduct(ProductCreateRequest productCreateRequest);

	void updateProduct(ProductUpdateRequest productUpdateRequest);

	void updateProductStatus(Long productId, String status);

	void updateVariantStockQuantityAndPrice(String uuid, UpdateVariantStockQuantityAndPriceRequest request);

	void updateVariantStock(Long variantId, Integer stockQuantity);
}


