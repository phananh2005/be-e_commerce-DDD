package com.phananh.e_commerce.presentation.controller;

import com.phananh.e_commerce.presentation.dto.request.user.UserChangePasswordRequest;
import com.phananh.e_commerce.presentation.dto.request.user.UserUpdateRequest;
import com.phananh.e_commerce.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.presentation.dto.response.user.CustomerUserResponse;
import com.phananh.e_commerce.application.service.UserService;
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
@Tag(name = "NgÆ°á»i dÃ¹ng", description = "CÃ¡c API quáº£n lÃ½ thÃ´ng tin tÃ i khoáº£n ngÆ°á»i dÃ¹ng")
public class UserController {

    UserService userService;

    //customer
    @GetMapping("/my-info")
    @Operation(summary = "Láº¥y thÃ´ng tin cÃ¡ nhÃ¢n", description = "Láº¥y thÃ´ng tin há»“ sÆ¡ cá»§a ngÆ°á»i dÃ¹ng hiá»‡n Ä‘ang Ä‘Äƒng nháº­p")
    public ResponseEntity<ApiResponse<CustomerUserResponse>> getMyInfo() {
        return null;
    }

    @PutMapping("/update-info")
    @Operation(summary = "Cáº­p nháº­t thÃ´ng tin cÃ¡ nhÃ¢n", description = "Cáº­p nháº­t thÃ´ng tin há»“ sÆ¡ cá»§a ngÆ°á»i dÃ¹ng hiá»‡n Ä‘ang Ä‘Äƒng nháº­p")
    public ResponseEntity<ApiResponse<CustomerUserResponse>> updateMyInfo(@RequestBody UserUpdateRequest userUpdateRequest) {
        return null;
    }

    @PatchMapping("/change-password")
    @Operation(summary = "", description = "Cáº­p nháº­t thÃ´ng tin há»“ sÆ¡ cá»§a ngÆ°á»i dÃ¹ng hiá»‡n Ä‘ang Ä‘Äƒng nháº­p")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(userChangePasswordRequest);
        return null;
    }

    //admin
    @GetMapping
    public ResponseEntity<ApiResponse<CustomerUserResponse>> getUsers() {
        return null;
    }
}

