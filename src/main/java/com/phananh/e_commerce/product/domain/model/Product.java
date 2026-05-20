package com.phananh.e_commerce.product.domain.model;

import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductVariant> variants = new HashSet<>();

    public static Product create(String name,
                                 String description,
                                 Long categoryId,
                                 Long brandId,
                                 String avatarUrl,
                                 ProductStatus status) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("Category id cannot be null");
        }
        if (brandId == null) {
            throw new IllegalArgumentException("Brand id cannot be null");
        }
        if (StringUtils.isBlank(avatarUrl)) {
            throw new IllegalArgumentException("Product avatar url cannot be null or blank");
        }

        return Product.builder()
                .name(name.trim())
                .description(StringUtils.isBlank(description) ? null : description.trim())
                .avatarUrl(avatarUrl.trim())
                .status(status == null ? ProductStatus.DRAFT : status)
                .categoryId(categoryId)
                .brandId(brandId)
                .build();
    }

    public String buildProductAvatarPublicId() {
        return "product-" + id + "-avatar";
    }

    public void updateAvatarUrl(String avatarUrl) {
        if (StringUtils.isBlank(avatarUrl)) {
            throw new IllegalArgumentException("Product avatar url cannot be null or blank");
        }
        this.avatarUrl = avatarUrl.trim();
    }

    public void replaceVariants(Set<ProductVariant> variants) {
        this.variants = variants == null ? new HashSet<>() : variants;
    }
}


