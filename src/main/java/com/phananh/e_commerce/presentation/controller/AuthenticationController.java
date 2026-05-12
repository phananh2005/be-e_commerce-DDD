package com.phananh.e_commerce.presentation.controller;

import com.phananh.e_commerce.presentation.dto.request.auth.AuthenticationRequest;
import com.phananh.e_commerce.presentation.dto.request.auth.LogoutRequest;
import com.phananh.e_commerce.presentation.dto.request.auth.RefreshTokenRequest;
import com.phananh.e_commerce.presentation.dto.request.auth.RegisterRequest;
import com.phananh.e_commerce.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.presentation.dto.response.auth.AuthTokenResponse;
import com.phananh.e_commerce.presentation.dto.response.auth.LogoutResponse;
import com.phananh.e_commerce.application.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "XÃ¡c thá»±c", description = "ÄÄƒng nháº­p, Ä‘Äƒng kÃ½ vÃ  quáº£n lÃ½ phiÃªn lÃ m viá»‡c")
public class AuthenticationController {

    AuthenticationService authenticationService;

    @Operation(summary = "ÄÄƒng nháº­p")
    @PostMapping("/login")
    public ApiResponse<AuthTokenResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthTokenResponse>builder()
                .message("Login successful")
                .result(authenticationService.login(request))
                .build();
    }

    @Operation(summary = "ÄÄƒng kÃ½ tÃ i khoáº£n má»›i")
    @PostMapping("/register")
    public ApiResponse<AuthTokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.<AuthTokenResponse>builder()
                .message("Register successful")
                .result(authenticationService.register(request))
                .build();
    }

    @Operation(summary = "LÃ m má»›i token")
    @PostMapping("/refresh")
    public ApiResponse<AuthTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.<AuthTokenResponse>builder()
                .message("Refresh token successful")
                .result(authenticationService.refreshToken(request))
                .build();
    }

    @Operation(summary = "ÄÄƒng xuáº¥t")
    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout(@Valid @RequestBody LogoutRequest request) {
        return ApiResponse.<LogoutResponse>builder()
                .message("Logout successful")
                .result(authenticationService.logout(request))
                .build();
    }

//    @PostMapping("/introspect")
//    public ApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request) {
//        return ApiResponse.<IntrospectResponse>builder()
//                .message("Introspect successful")
//                .result(authenticationService.introspect(request))
//                .build();
//    }



}

