package com.phananh.e_commerce.application.service.impl;

import com.phananh.e_commerce.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.presentation.dto.request.brand.BrandUpdateRequest;
import com.phananh.e_commerce.presentation.dto.response.brand.BrandResponse;
import com.phananh.e_commerce.application.exception.AppException;
import com.phananh.e_commerce.application.exception.ErrorCode;
import com.phananh.e_commerce.presentation.mapper.BrandMapper;
import com.phananh.e_commerce.domain.model.entity.Brand;
import com.phananh.e_commerce.infrastructure.persistence.repository.BrandRepository;
import com.phananh.e_commerce.application.service.BrandService;
import com.phananh.e_commerce.application.service.CloudinaryService;
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

    BrandRepository brandRepository;
    BrandMapper brandMapper;
    CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getBrandsBySearch(BrandSearchRequest request) {
        String keyword = request.getKeyword() == null ? "" : request.getKeyword().trim();
        int page = request.getPage() == null ? 0 : Math.max(request.getPage(), 0);
        int size = request.getSize() == null ? 20 : Math.max(request.getSize(), 1);

        return brandRepository.findByNameContainingIgnoreCase(
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
        if (brandRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new AppException(ErrorCode.CONFLICT);
        }

        Brand brand = Brand.builder()
                .name(normalizedName)
                .build();

        brand = brandRepository.saveAndFlush(brand);

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

        brandRepository.save(brand);
        return brandMapper.toResponse(brand);
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(BrandUpdateRequest request) {
        String normalizedName = normalizeName(request.getName());
        Brand brand = brandRepository.findById(request.getBrandId())
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

        brandRepository.save(brand);
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

