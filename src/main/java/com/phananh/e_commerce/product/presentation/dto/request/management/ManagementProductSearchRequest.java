package com.phananh.e_commerce.product.presentation.dto.request.management;

import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class ManagementProductSearchRequest {

    private String productSearch;

    private List<Long> categoryIds;

    private List<Long> brandIds;

    private ProductStatus status;

    @Min(value = 0, message = "Page must be >= 0")
    private Integer page;

    @Min(value = 1, message = "Size must be >= 1")
    private Integer size;

    private String sortBy;

    private String sortType;
}


