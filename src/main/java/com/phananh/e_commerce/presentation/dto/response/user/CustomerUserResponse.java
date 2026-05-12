package com.phananh.e_commerce.presentation.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerUserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
}

