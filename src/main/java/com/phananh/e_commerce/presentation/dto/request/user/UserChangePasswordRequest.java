package com.phananh.e_commerce.presentation.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangePasswordRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Old Password is required")
    private String oldPassword;

    @NotBlank(message = "New Password is required")
    private String newPassword;
}

