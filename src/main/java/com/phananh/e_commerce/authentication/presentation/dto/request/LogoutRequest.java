package com.phananh.e_commerce.authentication.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {

    @NotBlank(message = "Access Token is required")
    private String accessToken;

    @NotBlank(message = "Refresh Token is required")
    private String refreshToken;
}
