package com.phananh.e_commerce.presentation.dto.response.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutResponse {
    private boolean success;
}


