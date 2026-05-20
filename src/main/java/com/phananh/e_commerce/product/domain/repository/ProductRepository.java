package com.phananh.e_commerce.product.domain.repository;

import com.phananh.e_commerce.product.application.dto.query.ProductSearchQuery;
import com.phananh.e_commerce.product.domain.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Page<Product> getProductsActiveBySearch(ProductSearchQuery productSearchQuery);
    Optional<Product> getById(Long id);
    List<Product> getAllProductsBySearch(ProductSearchQuery productSearchQuery);
}

