package com.phananh.e_commerce.productcatalog.presentation.dto.request.category;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CategorySearchRequest {

    private String name;

    @Min(value = 0, message = "Page must be >= 0")
    private Integer page;

    @Min(value = 1, message = "Size must be >= 1")
    private Integer size;

    private String sortBy;

    private String sortType;

    private Boolean enabled;
}


