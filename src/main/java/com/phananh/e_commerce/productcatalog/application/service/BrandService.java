package com.phananh.e_commerce.productcatalog.application.service;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.brand.BrandResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {
    Page<BrandResponse> getBrandsBySearch(BrandSearchRequest request);

    void createBrand(BrandCreateRequest request);

    BrandResponse updateBrand(BrandUpdateRequest request);
}


