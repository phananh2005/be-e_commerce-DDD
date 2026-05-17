package com.phananh.e_commerce.productcatalog.domain.repository;

import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BrandRepository {
    Page<Brand> getListBrandByKeyword(String keyword, Pageable pageable);
    Page<Brand> getListBrand(Pageable pageable);
    Brand saveAndFlush(Brand brand);
    void save(Brand brand);
    boolean existsByNameIgnoreCase(String normalizedName);
    Optional<Brand> getById(Long brandId);
}
