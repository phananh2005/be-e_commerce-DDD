package com.phananh.e_commerce.productcatalog.application.service.impl;

import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.productcatalog.domain.repository.BrandRepository;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.brand.BrandResponse;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.productcatalog.application.mapper.BrandMapper;
import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata.SpringDataBrandRepository;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BrandServiceImpl implements BrandService {

    BrandRepository brandRepository;
    BrandMapper brandMapper;
    CloudinaryService cloudinaryService;

    private Pageable getPageable(BrandSearchRequest request){
        request.setSortBy(request.getSortBy() != null ? request.getSortBy() : "id");
        request.setSortType(request.getSortType() != null ? request.getSortType() : "asc");

        return PageRequest.of(request.getPage() - 1, request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortType()), request.getSortBy()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandResponse> getBrandsBySearch(BrandSearchRequest request) {
        Pageable pageable = getPageable(request);
        String keyword = StringUtils.normalizeName(request.getKeyword());

        return brandRepository.getListBrand(keyword, pageable)
                .map(brandMapper::toResponse);
    }

    @Override
    @Transactional
    public void createBrand(BrandCreateRequest request) {
        String normalizedName = StringUtils.normalizeName(request.getName());
        if (brandRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new AppException(ErrorCode.CONFLICT);
        }

        Brand brand = Brand.create(normalizedName, request.getDescription());

        brand = brandRepository.saveAndFlush(brand);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String publicId = brand.buildBrandAvatarPublicId();
                String imageUrl = cloudinaryService.uploadFile(request.getImage(), "brands", publicId);
                brand.updateImage(imageUrl);
            } catch (IOException e) {
                log.error("Error uploading brand image", e);
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(BrandUpdateRequest request) {
        String normalizedName = normalizeName(request.getName());
        Brand brand = springDataBrandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        brand.setName(normalizedName);

        // Handle image upload
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String publicId = buildBrandAvatarPublicId(brand.getId());
                String imageUrl = cloudinaryService.uploadFile(request.getImage(), "brands", publicId);
                brand.setImageUrl(imageUrl);
            } catch (IOException e) {
                log.error("Error uploading brand image", e);
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        springDataBrandRepository.save(brand);
        return brandMapper.toResponse(brand);
    }


    private String buildBrandAvatarPublicId(Long brandId) {
        return "brand-" + brandId + "-avatar";
    }
}


