package com.phananh.e_commerce.infrastructure.config;

import com.phananh.e_commerce.domain.model.entity.Role;
import com.phananh.e_commerce.domain.model.entity.User;
import com.phananh.e_commerce.domain.model.enums.RoleName;
import com.phananh.e_commerce.infrastructure.persistence.repository.RoleRepository;
import com.phananh.e_commerce.infrastructure.persistence.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepo, RoleRepository roleRepo) {
        log.info("CONFIG: Init Application");
        return args -> {
            Role adminRole = roleRepo.findByName(RoleName.ADMIN)
                    .orElseGet(() -> {
                        Role newRole = Role.builder().name(RoleName.ADMIN).build();
                        Role savedRole = roleRepo.save(newRole);
                        log.info("ADMIN role has been created");
                        return savedRole;
                    });

            if (!userRepo.existsByRoles_Name(RoleName.ADMIN)) {
                Optional<User> existingAdminUser = userRepo.findByUsername("admin");

                if (existingAdminUser.isPresent()) {
                    User user = existingAdminUser.get();
                    user.getRoles().add(adminRole);
                    userRepo.save(user);
                    log.info("ADMIN role has been assigned to existing user: admin");
                    return;
                }

                User user =
                        User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin"))
                                .fullName("ADMIN-MANAGERMENT")
                                .enabled(true)
                                .roles(new HashSet<>(Set.of(adminRole)))
                                .build();
                userRepo.save(user);
                log.info("admin account has been created with default: (username,password) - (admin,admin) , please change it !");
            } else {
                log.info("Admin account already exists");
            }
        };
    }
}

