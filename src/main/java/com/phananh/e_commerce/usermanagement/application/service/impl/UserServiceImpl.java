package com.phananh.e_commerce.usermanagement.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.core.util.SecurityUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.usermanagement.application.dto.query.UserSearchQuery;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserResponse;
import com.phananh.e_commerce.usermanagement.application.mapper.UserMapper;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import com.phananh.e_commerce.core.util.PasswordUtils;
import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.UserCredentials;
import com.phananh.e_commerce.usermanagement.domain.model.UserInfo;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import com.phananh.e_commerce.usermanagement.domain.repository.UserRepository;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo() {
        String userName = SecurityUtils.getCurrentUserName();
        User user = userRepository.getByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserInfoResponse(user);
    }

    @Override
    @Transactional
    public void updateMyInfo(UserInfoUpdateRequest userInfoUpdateRequest) {
        String userName = SecurityUtils.getCurrentUserName();
        User user = userRepository.getByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserInfo updatedUserInfo = UserInfo.builder()
                .email(userInfoUpdateRequest.getEmail())
                .fullName(userInfoUpdateRequest.getFullName())
                .phoneNumber(userInfoUpdateRequest.getPhoneNumber())
                .address(userInfoUpdateRequest.getAddress())
                .build();
        user.updateInfo(updatedUserInfo);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(UserChangePasswordRequest userChangePasswordRequest) {
        String userName = SecurityUtils.getCurrentUserName();
        User user = userRepository.getByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.changePassword(
                userChangePasswordRequest.getOldPassword(),
                userChangePasswordRequest.getNewPassword()
        );

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserInfoResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getIdByUserName(String userName) {
        User user = userRepository.getByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(ManagementUserQueryRequest request) {
        int page = PageUtils.getPageNumber(request.getPage());
        int size = PageUtils.getPageSize(request.getSize());
        String sortBy = PageUtils.getSortBy(request.getSortBy());
        String sortType = PageUtils.getSortType(request.getSortType());
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by(Sort.Direction.fromString(sortType), sortBy));

        UserSearchQuery userSearchQuery = UserSearchQuery.builder()
                .keyword(request.getKeyword() == null ? null : request.getKeyword().trim())
                .roleNames(request.getRoleNames())
                .enabled(request.getEnabled())
                .createdDateFrom(request.getCreatedDateFrom())
                .createdDateTo(request.getCreatedDateTo())
                .modifiedDateFrom(request.getModifiedDateFrom())
                .modifiedDateTo(request.getModifiedDateTo())
                .pageable(pageable)
                .build();

        return userRepository.getListUsersBySearch(userSearchQuery)
                .map(userMapper::toResponse);
    }

    @Override
    @Transactional
    public void updateUserRole(UserRoleUpdateRequest userRoleUpdateRequest) {
        User user = userRepository.getById(userRoleUpdateRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Set<Role> roles = userRepository.getRolesByRoleNames(userRoleUpdateRequest.getRoleNames());

        user.updateRoles(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, String status) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (StringUtils.isBlank(status)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if ("ACTIVE".equalsIgnoreCase(status)) user.active();
        else if ("INACTIVE".equalsIgnoreCase(status)) user.inactive();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createUser(CreateUserRequest request) {
        RoleName role;
        try {
            role = RoleName.valueOf(request.getRoleName());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if (userRepository.getByUserName(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        Set<Role> roles = userRepository.getRolesByRoleNames(Set.of(role));

        UserInfo userInfo = UserInfo.builder()
                .email(request.getEmail())
                .address(request.getAddress())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        User user = User.builder()
                .credentials(new UserCredentials(
                        request.getUsername(),
                        PasswordUtils.encode(request.getPassword()),
                        true))
                .info(userInfo)
                .roles(roles)
                .build();

        userRepository.save(user);
    }

}

