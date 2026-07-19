package com.phananh.e_commerce.usermanagement.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdateRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;

    private String email;
}


