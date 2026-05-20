package com.phananh.e_commerce.product.domain.model;

import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;
import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_variants")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "sku_code", unique = true, nullable = false)
    private String skuCode;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "variant_attribute_values",
            joinColumns = @JoinColumn(name = "variant_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id", nullable = false)
    )
    private Set<AttributeValue> attributeValues = new HashSet<>();

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VariantImage> images = new HashSet<>();

    public static ProductVariant create(Product product,
                                        String skuCode,
                                        BigDecimal price,
                                        Integer stockQuantity,
                                        Set<VariantImage> images,
                                        Set<AttributeValue> attributeValues) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (StringUtils.isBlank(skuCode)) {
            throw new IllegalArgumentException("SKU code cannot be null or blank");
        }
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }
        if (stockQuantity == null || stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity must be >= 0");
        }

        return ProductVariant.builder()
                .product(product)
                .skuCode(skuCode.trim())
                .price(price)
                .stockQuantity(stockQuantity)
                .images(images == null ? new HashSet<>() : images)
                .attributeValues(attributeValues == null ? new HashSet<>() : attributeValues)
                .build();
    }
}


