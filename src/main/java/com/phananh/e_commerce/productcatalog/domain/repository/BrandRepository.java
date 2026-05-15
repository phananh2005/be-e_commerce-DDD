package com.phananh.e_commerce.productcatalog.domain.repository;

import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandRepository {
    Page<Brand> getListBrand(String keyword, Pageable pageable);
    Brand saveAndFlush(Brand brand);
    void save(Brand brand);
    boolean existsByNameIgnoreCase(String normalizedName);
}
