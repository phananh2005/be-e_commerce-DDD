package com.phananh.e_commerce.usermanagement.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserResponse;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.AdminUserQueryRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserChangePasswordRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserInfoUpdateRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserRoleUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Người dùng", description = "Các API quản lý thông tin tài khoản người dùng")
public class UserController {

    UserService userService;

    //customer
    @GetMapping("/my-info")
    @Operation(summary = "Lấy thông tin cá nhân")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo() {
        ApiResponse<UserInfoResponse> apiResponse = ApiResponse.<UserInfoResponse>builder()
                .result(userService.getMyInfo())
                .message("Get user info successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/update-info")
    @Operation(summary = "Cập nhật thông tin cá nhân")
    public ResponseEntity<?> updateMyInfo(@RequestBody @Valid UserInfoUpdateRequest userInfoUpdateRequest) {
        userService.updateMyInfo(userInfoUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu")
    public ResponseEntity<?> changePassword(@RequestBody @Valid UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(userChangePasswordRequest);
        return ResponseEntity.noContent().build();
    }

    //staff
    @GetMapping("/customer/info/{id}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCustomerInfo(@PathVariable Long id) {
        ApiResponse<UserInfoResponse> apiResponse = ApiResponse.<UserInfoResponse>builder()
                .result(userService.getUserInfo(id))
                .message("Get customer info successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    //admin
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(@ModelAttribute @Valid AdminUserQueryRequest adminUserQueryRequest) {
        Page<UserResponse> users = userService.getAllUsers(adminUserQueryRequest);
        ApiResponse<Page<UserResponse>> apiResponse = ApiResponse.<Page<UserResponse>>builder()
                .result(users)
                .message("Get all users successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/update-role")
    public ResponseEntity<?> updateUserRole(@RequestBody @Valid UserRoleUpdateRequest userRoleUpdateRequest) {
        userService.updateUserRole(userRoleUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-status")
    public ResponseEntity<?> updateUserStatus(@RequestBody @Valid UserRoleUpdateRequest userRoleUpdateRequest) {
        userService.updateUserRole(userRoleUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
