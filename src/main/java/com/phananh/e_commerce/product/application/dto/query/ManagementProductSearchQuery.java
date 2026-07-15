package com.phananh.e_commerce.product.application.dto.query;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
public class ManagementProductSearchQuery {
    private String keyword;
    private List<Long> categoryIds;
    private List<Long> brandIds;
    private Double minPrice;
    private Double maxPrice;
    private Integer minRating;
    private Pageable pageable;
}

