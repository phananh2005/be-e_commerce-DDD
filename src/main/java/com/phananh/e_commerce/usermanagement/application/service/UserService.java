package com.phananh.e_commerce.usermanagement.application.service;

import com.phananh.e_commerce.usermanagement.application.dto.response.RoleResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponseForManagement;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserSummaryForManagementResponse;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    long countUsers();

    UserInfoResponse getMyInfo();
    void updateMyInfo(UserInfoUpdateRequest userInfoUpdateRequest);
    void changePassword(UserChangePasswordRequest userChangePasswordRequest);
    UserInfoResponseForManagement getUserInfo(Long id);
    String getUsernameByUserId(Long userId);
    Long getIdByUserName(String userName);
    Page<UserSummaryForManagementResponse> getAllUsers(UserQueryRequest userQueryRequest);
    List<RoleResponse> getRoles();
    void updateUserRole(UserRoleUpdateRequest userRoleUpdateRequest);
    void updateUserStatus(Long userId, String status);
    void createUser(CreateUserRequest request);
}

