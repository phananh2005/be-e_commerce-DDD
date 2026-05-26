package com.phananh.e_commerce.usermanagement.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserResponse;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.AdminUserQueryRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserRoleUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quản trị - Người dùng", description = "API quản trị dành cho quản lý người dùng")
public class AdminUserController {

    UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(@ModelAttribute @Valid AdminUserQueryRequest adminUserQueryRequest) {
        Page<UserResponse> users = userService.getAllUsers(adminUserQueryRequest);
        ApiResponse<Page<UserResponse>> apiResponse = ApiResponse.<Page<UserResponse>>builder()
                .result(users)
                .message("Get all users successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/customer/info/{id}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCustomerInfo(@PathVariable Long id) {
        ApiResponse<UserInfoResponse> apiResponse = ApiResponse.<UserInfoResponse>builder()
                .result(userService.getUserInfo(id))
                .message("Get customer info successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/update-role")
    public ResponseEntity<?> updateUserRole(@RequestBody @Valid UserRoleUpdateRequest userRoleUpdateRequest) {
        userService.updateUserRole(userRoleUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("id") Long userId,
                                              @PathVariable String status) {
        userService.updateUserStatus(userId, status);
        return ResponseEntity.noContent().build();
    }

}

