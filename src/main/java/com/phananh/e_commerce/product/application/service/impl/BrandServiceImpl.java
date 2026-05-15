package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import com.phananh.e_commerce.product.application.mapper.BrandMapper;
import com.phananh.e_commerce.product.application.service.BrandService;
import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata.SpringDataBrandRepository;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandUpdateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.response.brand.BrandResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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

    SpringDataBrandRepository springDataBrandRepository;
    BrandMapper brandMapper;
    CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getBrandsBySearch(BrandSearchRequest request) {
        String keyword = request.getKeyword() == null ? "" : request.getKeyword().trim();
        int page = request.getPage() == null ? 0 : Math.max(request.getPage(), 0);
        int size = request.getSize() == null ? 20 : Math.max(request.getSize(), 1);

        return springDataBrandRepository.findByNameContainingIgnoreCase(
                        keyword,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"))
                )
                .stream()
                .map(brandMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BrandResponse createBrand(BrandCreateRequest request) {
        String normalizedName = normalizeName(request.getName());
        if (springDataBrandRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new AppException(ErrorCode.CONFLICT);
        }

        Brand brand = Brand.builder()
                .name(normalizedName)
                .build();

        brand = springDataBrandRepository.saveAndFlush(brand);

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

    private String normalizeName(String name) {
        String normalizedName = name == null ? "" : name.trim();
        if (normalizedName.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        return normalizedName;
    }

    private String buildBrandAvatarPublicId(Long brandId) {
        return "brand-" + brandId + "-avatar";
    }
}


