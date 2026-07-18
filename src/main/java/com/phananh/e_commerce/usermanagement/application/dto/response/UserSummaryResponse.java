package com.phananh.e_commerce.usermanagement.application.dto.response;

import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserSummaryResponse {
    private Long id;
    private String username;
    private String fullName;
    private String phoneNumber;
    private Set<RoleName> roles;
    private Boolean isEnabled;
}
