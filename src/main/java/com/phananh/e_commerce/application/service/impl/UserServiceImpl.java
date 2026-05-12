package com.phananh.e_commerce.application.service.impl;

import com.phananh.e_commerce.presentation.dto.request.user.UserChangePasswordRequest;
import com.phananh.e_commerce.presentation.dto.response.user.CustomerUserResponse;
import com.phananh.e_commerce.infrastructure.persistence.repository.UserRepository;
import com.phananh.e_commerce.application.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public CustomerUserResponse getMyInfo() {
        return null;
    }

    @Override
    public CustomerUserResponse updateMyInfo() {
        return null;
    }

    @Override
    public void changePassword(UserChangePasswordRequest userChangePasswordRequest) {

    }
}

