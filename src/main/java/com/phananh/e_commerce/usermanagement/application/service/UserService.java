package com.phananh.e_commerce.usermanagement.application.service;

import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserResponse;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.*;
import org.springframework.data.domain.Page;

public interface UserService {
    long countUsers();

    UserInfoResponse getMyInfo();
    void updateMyInfo(UserInfoUpdateRequest userInfoUpdateRequest);
    void changePassword(UserChangePasswordRequest userChangePasswordRequest);
    UserResponse getUserInfo(Long id);
    Long getIdByUserName(String userName);
    Page<UserResponse> getAllUsers(UserQueryRequest userQueryRequest);
    void updateUserRole(UserRoleUpdateRequest userRoleUpdateRequest);
    void updateUserStatus(Long userId, String status);
    void createUser(CreateUserRequest request);
}

