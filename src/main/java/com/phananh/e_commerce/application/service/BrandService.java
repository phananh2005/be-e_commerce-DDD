package com.phananh.e_commerce.application.service;

import com.phananh.e_commerce.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.presentation.dto.request.brand.BrandUpdateRequest;
import com.phananh.e_commerce.presentation.dto.response.brand.BrandResponse;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getBrandsBySearch(BrandSearchRequest request);

    BrandResponse createBrand(BrandCreateRequest request);

    BrandResponse updateBrand(BrandUpdateRequest request);
}

