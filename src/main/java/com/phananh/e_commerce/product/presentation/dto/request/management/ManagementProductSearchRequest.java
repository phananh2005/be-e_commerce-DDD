package com.phananh.e_commerce.product.presentation.dto.request.management;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class ManagementProductSearchRequest {

    private String productSearch;

    private List<Long> categoryIds;

    private List<Long> brandIds;

    @Min(value = 0, message = "Page must be >= 0")
    private Integer page;

    @Min(value = 1, message = "Size must be >= 1")
    private Integer size;

    private String sortBy;

    private String sortType;
}


