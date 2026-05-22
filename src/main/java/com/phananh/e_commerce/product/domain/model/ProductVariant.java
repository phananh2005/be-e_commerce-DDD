package com.phananh.e_commerce.product.domain.model;

import com.phananh.e_commerce.product.application.dto.command.ProductVariantCreateCommand;
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

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private Set<VariantImage> images = new HashSet<>();

    public static ProductVariant create(ProductVariantCreateCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        if (command.getProduct() == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        return ProductVariant.builder()
                .product(command.getProduct())
                .skuCode(StringUtils.isBlank(command.getSkuCode()) ? "empty" : command.getSkuCode().trim())
                .price(command.getPrice().signum() < 0 ? BigDecimal.ZERO : command.getPrice())
                .stockQuantity(command.getStockQuantity() == null || command.getStockQuantity() < 0 ? 0 : command.getStockQuantity())
                .images(command.getImages() == null ? new HashSet<>() : command.getImages())
                .attributeValues(command.getAttributeValues() == null ? new HashSet<>() : command.getAttributeValues())
                .build();
    }
}


