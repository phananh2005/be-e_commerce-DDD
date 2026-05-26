package com.phananh.e_commerce.usermanagement.infrastructure.config;

import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.UserCredentials;
import com.phananh.e_commerce.usermanagement.domain.model.UserInfo;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataRoleRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements ApplicationRunner {

    private final SpringDataUserRepository springDataUserRepository;
    private final SpringDataRoleRepository springDataRoleRepository;

    @Value("${app.seed.admin.username:admin}")
    private String username;

    @Value("${app.seed.admin.password:Admin@12345}")
    private String password;

    @Value("${app.seed.admin.email:admin@ecommerce.local}")
    private String email;

    @Value("${app.seed.admin.full-name:System Administrator}")
    private String fullName;

    @Value("${app.seed.admin.phone-number:0000000000}")
    private String phoneNumber;

    @Value("${app.seed.admin.address:System}")
    private String address;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        if (springDataUserRepository.count() > 0) {
            return;
        }

        Role adminRole = springDataRoleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseGet(() -> springDataRoleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build()));

        User adminUser = new User();
        setField(adminUser, "credentials", new UserCredentials(username, password, true));
        setField(adminUser, "info", new UserInfo(fullName, email, address, phoneNumber));
        setField(adminUser, "roles", new HashSet<>(Set.of(adminRole)));

        springDataUserRepository.save(adminUser);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Cannot set admin account field: " + fieldName, ex);
        }
    }
}


