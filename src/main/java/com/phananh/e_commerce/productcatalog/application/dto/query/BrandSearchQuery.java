package com.phananh.e_commerce.productcatalog.application.dto.query;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@Builder
public class BrandSearchQuery {
    private String name;
    private Boolean enabled;
    private Pageable pageable;
}
