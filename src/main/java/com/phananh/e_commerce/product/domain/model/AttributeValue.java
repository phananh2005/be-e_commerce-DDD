package com.phananh.e_commerce.product.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attribute_values")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value_name", nullable = false)
    private String valueName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribute_id", nullable = false)
    private ProductAttribute attribute;

//    @ManyToMany(mappedBy = "attributeValues", fetch = FetchType.LAZY)
//    private Set<ProductVariant> variants = new HashSet<>();
}


