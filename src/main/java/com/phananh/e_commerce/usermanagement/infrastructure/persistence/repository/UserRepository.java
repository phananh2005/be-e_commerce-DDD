package com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository;

import com.phananh.e_commerce.usermanagement.domain.model.entity.User;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRoles_Name(RoleName roleName);
}


