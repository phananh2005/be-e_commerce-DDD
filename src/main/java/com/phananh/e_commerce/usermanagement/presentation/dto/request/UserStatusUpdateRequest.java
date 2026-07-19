package com.phananh.e_commerce.usermanagement.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusUpdateRequest {
    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "User status is required")
    private Boolean status;
}
