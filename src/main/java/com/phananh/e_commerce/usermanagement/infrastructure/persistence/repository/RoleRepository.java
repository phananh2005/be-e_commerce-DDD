package com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository;

import com.phananh.e_commerce.usermanagement.domain.model.entity.Role;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}


