package com.phananh.e_commerce.productcatalog.application.service;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandImageUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandInfoUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.brand.BrandResponse;
import org.springframework.data.domain.Page;

public interface BrandService {
    Page<BrandResponse> getBrandsBySearch(BrandSearchRequest request);

    void createBrand(BrandCreateRequest request);

    void updateBrandInfo(BrandInfoUpdateRequest request);

    void updateBrandImage(BrandImageUpdateRequest request);
}


