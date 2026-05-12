package com.phananh.e_commerce.presentation.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountStatusUpdateRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    private String reason;
}



