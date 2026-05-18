package com.phananh.e_commerce.product.presentation.dto.request.brand;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class BrandSearchRequest {

    private String keyword;

    @JsonSetter(nulls = Nulls.SKIP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdDateFrom;

    @JsonSetter(nulls = Nulls.SKIP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdDateTo;

    @JsonSetter(nulls = Nulls.SKIP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modifiedDateFrom;

    @JsonSetter(nulls = Nulls.SKIP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modifiedDateTo;

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


