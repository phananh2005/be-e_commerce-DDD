package com.phananh.e_commerce.productcatalog.application.service;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.*;
import com.phananh.e_commerce.productcatalog.application.dto.response.BrandResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getBrandActive();
    Page<BrandResponse> getBrandsBySearch(BrandSearchRequest request);

    void createBrand(BrandCreateRequest request);

    void updateBrandInfo(BrandInfoUpdateRequest request);

    void updateBrandImage(BrandImageUpdateRequest request);

    void updateBrandStatus(BrandStatusUpdateRequest request);
}


