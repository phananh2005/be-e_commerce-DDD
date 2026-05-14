package com.phananh.e_commerce.usermanagement.application.service;

import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserResponse;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.AdminUserQueryRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserChangePasswordRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserInfoUpdateRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserRoleUpdateRequest;
import org.springframework.data.domain.Page;

public interface UserService {
    UserInfoResponse getMyInfo();
    void updateMyInfo(UserInfoUpdateRequest userInfoUpdateRequest);
    void changePassword(UserChangePasswordRequest userChangePasswordRequest);
    UserInfoResponse getUserInfo(Long id);
    Page<UserResponse> getAllUsers(AdminUserQueryRequest adminUserQueryRequest);
    void updateUserRole(UserRoleUpdateRequest userRoleUpdateRequest);
}


