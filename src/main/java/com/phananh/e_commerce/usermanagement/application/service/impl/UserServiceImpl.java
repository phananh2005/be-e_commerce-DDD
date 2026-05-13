package com.phananh.e_commerce.usermanagement.application.service.impl;

import com.phananh.e_commerce.usermanagement.domain.model.entity.User;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.UserRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.RoleRepository;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void assignRole(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        user.getRoles().add(role);
        userRepository.save(user);
    }
}


