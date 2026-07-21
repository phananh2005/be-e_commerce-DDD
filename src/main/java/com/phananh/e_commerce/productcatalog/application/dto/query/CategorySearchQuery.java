package com.phananh.e_commerce.productcatalog.application.dto.query;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@Builder
public class CategorySearchQuery {
    private String name;
    private Pageable pageable;
}
