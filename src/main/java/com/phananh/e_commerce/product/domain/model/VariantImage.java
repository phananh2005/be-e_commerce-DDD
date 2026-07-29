package com.phananh.e_commerce.product.domain.model;

import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "variant_image")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VariantImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", columnDefinition = "BINARY(16)", unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "is_avatar", nullable = false)
    @Builder.Default
    private boolean isAvatar = false;

    public static VariantImage create(ProductVariant variant, String imageUrl, boolean isAvatar) {
        if (variant == null) {
            throw new IllegalArgumentException("Variant cannot be null");
        }
        if (StringUtils.isBlank(imageUrl)) {
            throw new IllegalArgumentException("Image url cannot be null or blank");
        }

        return VariantImage.builder()
                .uuid(UUID.randomUUID())
                .variant(variant)
                .imageUrl(imageUrl.trim())
                .isAvatar(isAvatar)
                .build();
    }

    @PrePersist
    public void generateUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
}


