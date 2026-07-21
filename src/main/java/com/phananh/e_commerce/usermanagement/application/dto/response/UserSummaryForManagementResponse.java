package com.phananh.e_commerce.usermanagement.application.dto.response;

import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserSummaryForManagementResponse {
    private String uuid;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Set<RoleName> roles;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
}
