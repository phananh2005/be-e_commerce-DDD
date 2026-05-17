package com.phananh.e_commerce.product.application.service;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandInfoUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.brand.BrandResponse;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getBrandsBySearch(BrandSearchRequest request);

    BrandResponse createBrand(BrandCreateRequest request);

    BrandResponse updateBrand(BrandInfoUpdateRequest request);
}


