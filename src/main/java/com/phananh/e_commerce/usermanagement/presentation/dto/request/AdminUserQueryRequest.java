package com.phananh.e_commerce.usermanagement.presentation.dto.request;

import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
public class AdminUserQueryRequest {
    private String keyword;
    private Set<RoleName> roleNames;
    private Boolean enabled;

    @Min(value = 1, message = "Page must be >= 0")
    private Integer page = 1;

    @Min(value = 1, message = "Size must be >= 1")
    private Integer size = 50;

    private String sortBy;
    private String sortType;
}


