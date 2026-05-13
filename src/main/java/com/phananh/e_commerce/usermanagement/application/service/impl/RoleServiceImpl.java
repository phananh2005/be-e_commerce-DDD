package com.phananh.e_commerce.usermanagement.application.service.impl;

import com.phananh.e_commerce.usermanagement.domain.model.entity.Role;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.RoleRepository;
import com.phananh.e_commerce.usermanagement.application.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleByName(RoleName roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    @Transactional
    public Role createRole(RoleName roleName) {
        if (roleRepository.findByName(roleName).isPresent()) {
            throw new AppException(ErrorCode.CONFLICT);
        }

        Role role = Role.builder()
                .name(roleName)
                .build();

        return roleRepository.save(role);
    }
}


