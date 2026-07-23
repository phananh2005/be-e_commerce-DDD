package com.phananh.e_commerce.productcatalog.infrastructure.persistence.specification;

import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BrandSearchSpecificationTest {

    @Test
    void filtersByEnabledWhenProvided() {
        Specification<Brand> specification = BrandSearchSpecification.hasEnabled(true);

        assertNotNull(specification);
    }
}
