package com.phananh.e_commerce.product.application.dto.query;

import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
public class ManagementProductSearchQuery {
    private String productSearch;
    private List<Long> categoryIds;
    private List<Long> brandIds;
    private ProductStatus status;
    private Pageable pageable;
}

