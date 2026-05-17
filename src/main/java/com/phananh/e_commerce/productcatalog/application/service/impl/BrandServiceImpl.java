package com.phananh.e_commerce.productcatalog.application.service.impl;

import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.productcatalog.domain.repository.BrandRepository;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandImageUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandInfoUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.brand.BrandResponse;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.productcatalog.application.mapper.BrandMapper;
import com.phananh.e_commerce.productcatalog.domain.model.Brand;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BrandServiceImpl implements BrandService {

    BrandRepository brandRepository;
    BrandMapper brandMapper;
    CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public Page<BrandResponse> getBrandsBySearch(BrandSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortType()), request.getSortBy()));

        if (StringUtils.isBlank(request.getKeyword()))
            return brandRepository.getListBrand(pageable)
                    .map(brandMapper::toResponse);

        else return brandRepository.getListBrandByKeyword(request.getKeyword().trim(), pageable)
                .map(brandMapper::toResponse);
    }

    @Override
    @Transactional
    public void createBrand(BrandCreateRequest request) {
        if (brandRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new AppException(ErrorCode.CONFLICT);
        }

        Brand brand = Brand.create(request.getName(), request.getDescription());

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
    public void updateBrandInfo(BrandInfoUpdateRequest request) {
        Brand brand = brandRepository.getById(request.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        brand.updateName(request.getName());
        brand.updateDescription(request.getDescription());

        brandRepository.save(brand);
    }

    @Override
    public void updateBrandImage(BrandImageUpdateRequest request) {
        Brand brand = brandRepository.getById(request.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        String publicId = brand.buildBrandAvatarPublicId();

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadFile(request.getImage(), "brands", publicId);
                brand.updateImage(imageUrl);
            } catch (IOException e) {
                log.error("Error uploading brand image", e);
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }
        else {
            try {
                cloudinaryService.deleteFile("brands/" + publicId);
                brand.removeImage();
            } catch (IOException e) {
                log.error("Error deleting brand image", e);
                throw new AppException(ErrorCode.FILE_DELETE_ERROR);
            }
        }
    }
}


