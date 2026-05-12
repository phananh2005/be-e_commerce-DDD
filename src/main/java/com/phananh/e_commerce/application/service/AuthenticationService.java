package com.phananh.e_commerce.application.service;

import com.phananh.e_commerce.presentation.dto.request.auth.AuthenticationRequest;
import com.phananh.e_commerce.presentation.dto.request.auth.LogoutRequest;
import com.phananh.e_commerce.presentation.dto.request.auth.RefreshTokenRequest;
import com.phananh.e_commerce.presentation.dto.request.auth.RegisterRequest;
import com.phananh.e_commerce.presentation.dto.response.auth.AuthTokenResponse;
import com.phananh.e_commerce.presentation.dto.response.auth.LogoutResponse;

public interface AuthenticationService {
	AuthTokenResponse login(AuthenticationRequest request);

	AuthTokenResponse register(RegisterRequest request);

	AuthTokenResponse refreshToken(RefreshTokenRequest request);

	LogoutResponse logout(LogoutRequest request);

//	IntrospectResponse introspect(IntrospectRequest request);
}


