package com.phananh.e_commerce.usermanagement.domain.repository;

import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface UserRepository {
    Optional<User> getById(Long id);
    Optional<User> getByUserName(String userName);
    void save(User user);
    Page<User> getListUsers(String keyword, Set<RoleName> roleName, Pageable pageable);
    Set<Role> getRolesByRoleNames(Set<RoleName> roleNames);
}
