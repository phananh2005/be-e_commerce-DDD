package com.phananh.e_commerce.product.infrastructure.persistence.specification;

import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductSearchSpecificationTest {

    @Test
    void hasStatus_acceptsProductStatusEnum() {
        assertNotNull(ProductSearchSpecification.hasStatus(ProductStatus.ACTIVE));
    }

    @Test
    void hasStatus_allowsNullAsNoOpFilter() {
        assertNotNull(ProductSearchSpecification.hasStatus(null));
    }
}
