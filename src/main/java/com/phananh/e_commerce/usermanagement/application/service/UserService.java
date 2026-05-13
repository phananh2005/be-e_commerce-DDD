package com.phananh.e_commerce.usermanagement.application.service;

import com.phananh.e_commerce.usermanagement.domain.model.entity.User;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    
    boolean userExists(String username);
    
    User saveUser(User user);
    
    void assignRole(Long userId, RoleName roleName);
}


