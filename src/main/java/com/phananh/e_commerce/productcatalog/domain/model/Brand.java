package com.phananh.e_commerce.productcatalog.domain.model;

import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;
import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brands")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    public static Brand create(String name, String description) {
        if(StringUtils.isNotBlank(name)) throw new IllegalArgumentException("Brand name cannot be null or blank");
        if(description != null){
            if(description.isBlank()) throw new IllegalArgumentException("Brand description cannot be null or blank");
            description = description.trim();
        }
        return Brand.builder()
                .name(name.trim())
                .description(description)
                .build();
    }

    public String buildBrandAvatarPublicId() {
        return "brand-" + id + "-avatar";
    }

    public Brand updateImage(String imageUrl){
        if(!StringUtils.isNotBlank(imageUrl)) throw new IllegalArgumentException("Image url cannot be null or blank");
        this.imageUrl = imageUrl;
        return this;
    }
}


