package com.phananh.e_commerce.productcatalog.presentation.dto.request.brand;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BrandSearchRequest {

    private String keyword;

    @Min(value = 1, message = "Page must be >= 0")
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer page = 1;

    @Min(value = 1, message = "Size must be >= 1")
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer size = 50;

    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(using = StringUtils.class)
    private String sortBy = "id";

    @JsonSetter(nulls = Nulls.SKIP)
    @JsonDeserialize(using = StringUtils.class)
    private String sortType = "acs";
}


