package com.phananh.e_commerce.usermanagement.application.dto.query;

import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserSearchQuery {
    private String userIdentifier;
    private String userInfo;
    private Set<RoleName> roleNames;
    private Boolean enabled;
    private LocalDateTime createdDateFrom;
    private LocalDateTime createdDateTo;
    private Pageable pageable;
}
