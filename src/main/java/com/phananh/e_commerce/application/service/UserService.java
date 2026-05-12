package com.phananh.e_commerce.application.service;

import com.phananh.e_commerce.presentation.dto.request.user.UserChangePasswordRequest;
import com.phananh.e_commerce.presentation.dto.response.user.CustomerUserResponse;

public interface UserService {
    CustomerUserResponse getMyInfo();
    CustomerUserResponse updateMyInfo();
    void changePassword(UserChangePasswordRequest userChangePasswordRequest);
}


