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

    @Column(nullable = false)
    private Boolean isEnabled;

    public static Brand create(String name, String description) {
        if(StringUtils.isBlank(name)) throw new IllegalArgumentException("Brand name cannot be null or blank");
        if(description != null){
            if(description.isBlank()) throw new IllegalArgumentException("Brand description cannot be blank");
            description = description.trim();
        }
        return Brand.builder()
                .name(name.trim())
                .description(description)
                .isEnabled(true)
                .build();
    }

    public String buildBrandAvatarPublicId() {
        return "brand-" + id + "-avatar";
    }

    public void updateImage(String imageUrl){
        if(StringUtils.isBlank(imageUrl)) throw new IllegalArgumentException("Brand image url cannot be null or blank");
        else this.imageUrl = imageUrl.trim();
    }

    public void removeImage(){
        this.imageUrl = null;
    }

    public void updateName(String name){
        if(StringUtils.isBlank(name)) throw new IllegalArgumentException("Brand name cannot be null or blank");
        this.name = name.trim();
    }

    public void updateDescription(String description){
        this.description = description == null ? "" : description.trim();
    }

    public void active() {this.isEnabled = true;}

    public void inactive() {this.isEnabled = false;}
}


