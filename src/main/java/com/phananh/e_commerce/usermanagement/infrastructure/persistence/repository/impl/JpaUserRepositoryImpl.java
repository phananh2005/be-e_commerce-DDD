package com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.impl;

import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import com.phananh.e_commerce.usermanagement.domain.repository.UserRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataRoleRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.specification.UserSearchSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class JpaUserRepositoryImpl implements UserRepository {

    SpringDataUserRepository springDataUserRepository;
    SpringDataRoleRepository springDataRoleRepository;

    @Override
    public Optional<User> getById(Long id) {
        return springDataUserRepository.findById(id);
    }

    @Override
    public Optional<User> getByUserName(String userName) {
        return springDataUserRepository.findByCredentialsUsername(userName);
    }

    @Override
    public void save(User user) {
        springDataUserRepository.save(user);
    }

    @Override
    public Page<User> getListUsers(String keyword, Set<RoleName> roleName, Pageable pageable) {
        Specification<User> specification = Specification.where(UserSearchSpecification.hasKeyword(keyword))
                .and(UserSearchSpecification.hasRoleName(roleName));
        return springDataUserRepository.findAll(specification, pageable);
    }

    @Override
    public Set<Role> getRolesByRoleNames(Set<RoleName> roleNames) {
        return springDataRoleRepository.findAllByNameIn(roleNames);
    }
}
