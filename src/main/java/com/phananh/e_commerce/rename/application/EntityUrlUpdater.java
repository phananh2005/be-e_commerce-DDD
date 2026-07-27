package com.phananh.e_commerce.rename.application;

import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataProductRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EntityUrlUpdater {
    private final SpringDataProductRepository productRepository;
    private final SpringDataUserRepository userRepository;


    public void updateUrl(String entityType, Long entityId, String newUrl) {
        switch (entityType) {
            case "PRODUCT":
                productRepository.findById(entityId).ifPresent(p -> {
                    p.updateAvatarUrl(newUrl);
                    productRepository.save(p);
                });
                break;
            case "USER":
                break;
            default:
                throw new IllegalArgumentException("Unsupported entity type: " + entityType);
        }
    }
}
