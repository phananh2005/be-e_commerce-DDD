package com.phananh.e_commerce.product.domain.repository;

import com.phananh.e_commerce.product.application.dto.query.ProductSearchQuery;
import com.phananh.e_commerce.product.application.dto.query.StaffProductSearchQuery;
import com.phananh.e_commerce.product.domain.model.AttributeValue;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductAttribute;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Page<Product> getProductsActiveBySearch(ProductSearchQuery productSearchQuery);
    Optional<Product> getById(Long id);
    Page<Product> getAllProductsBySearch(StaffProductSearchQuery productSearchQuery);
    List<ProductVariant> getVariantsByProductId(Long productId);
    Optional<ProductVariant> getVariantBySkuCode(String skuCode);
    Optional<ProductAttribute> getProductAttributesByName(String name);
    Product saveAndFlush(Product product);
    void save(Product product);
    void save(AttributeValue attributeValue);
    void save(ProductVariant variant);
}

