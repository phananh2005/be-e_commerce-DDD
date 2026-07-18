package com.phananh.e_commerce.usermanagement.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserSummaryResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponseForManagement;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.CreateUserRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserQueryRequest;
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
@RequestMapping("management/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quản trị - Người dùng", description = "API quản trị dành cho quản lý người dùng")
public class ManagementUserController {

    UserService userService;

    @Operation(summary = "Tạo người dùng mới")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lấy danh sách tất cả người dùng", description = "Tìm kiếm và phân trang danh sách người dùng")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserSummaryResponse>>> getAllUsers(@ModelAttribute @Valid UserQueryRequest userQueryRequest) {
        Page<UserSummaryResponse> users = userService.getAllUsers(userQueryRequest);
        ApiResponse<Page<UserSummaryResponse>> apiResponse = ApiResponse.<Page<UserSummaryResponse>>builder()
                .result(users)
                .message("Get all users successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Lấy thông tin người dùng", description = "Lấy thông tin chi tiết của một người dùng theo ID")
    @GetMapping("/info/{id}")
    public ResponseEntity<ApiResponse<UserInfoResponseForManagement>> getUserInfo(@PathVariable Long id) {
        ApiResponse<UserInfoResponseForManagement> apiResponse = ApiResponse.<UserInfoResponseForManagement>builder()
                .result(userService.getUserInfo(id))
                .message("Get user info successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Cập nhật vai trò người dùng", description = "Cập nhật thay đổi vai trò của người dùng")
    @PatchMapping("/update-role")
    public ResponseEntity<?> updateUserRole(@RequestBody @Valid UserRoleUpdateRequest userRoleUpdateRequest) {
        userService.updateUserRole(userRoleUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật trạng thái người dùng", description = "Kích hoạt hoặc vô hiệu hóa tài khoản người dùng")
    @PatchMapping("/{id}/{status}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("id") Long userId,
                                              @PathVariable String status) {
        userService.updateUserStatus(userId, status);
        return ResponseEntity.noContent().build();
    }

}
