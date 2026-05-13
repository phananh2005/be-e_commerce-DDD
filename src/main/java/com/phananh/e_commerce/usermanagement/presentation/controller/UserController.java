package com.phananh.e_commerce.usermanagement.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.user.UserChangePasswordRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.user.UserUpdateRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.response.user.CustomerUserResponse;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Người dùng", description = "Các API quản lý thông tin tài khoản người dùng")
public class UserController {

    UserService userService;    @GetMapping("/my-info")
    @Operation(summary = "Lấy thông tin cá nhân")
    public ResponseEntity<ApiResponse<CustomerUserResponse>> getMyInfo() {
        return null;
    }

    @PutMapping("/update-info")
    @Operation(summary = "Cập nhật thông tin cá nhân")
    public ResponseEntity<ApiResponse<CustomerUserResponse>> updateMyInfo(@RequestBody UserUpdateRequest userUpdateRequest) {
        return null;
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid UserChangePasswordRequest userChangePasswordRequest) {
        return null;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CustomerUserResponse>> getUsers() {
        return null;
    }
}
