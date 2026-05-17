//package com.phananh.e_commerce.core.infrastructure.config;
//
//import com.phananh.e_commerce.usermanagement.domain.model.Role;
//import com.phananh.e_commerce.usermanagement.domain.model.User;
//import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
//import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataRoleRepository;
//import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//@Configuration
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Slf4j
//public class ApplicationInitConfig {
//
//    PasswordEncoder passwordEncoder;
//
//    @Bean
//    @ConditionalOnProperty(prefix = "spring", value = "datasource.driver-class-name", havingValue = "com.mysql.cj.jdbc.Driver")
//    ApplicationRunner applicationRunner(SpringDataUserRepository userRepo, SpringDataRoleRepository roleRepo) {
//        log.info("CONFIG: Init Application");
//        return args -> {
//            Role adminRole = roleRepo.findByName(RoleName.ADMIN)
//                    .orElseGet(() -> {
//                        Role saved = roleRepo.save(Role.builder().name(RoleName.ADMIN).build());
//                        log.info("ADMIN role has been created");
//                        return saved;
//                    });
//
//            if (!userRepo.existsByRoles_Name(RoleName.ADMIN)) {
//                Optional<User> existing = userRepo.findByUsername("admin");
//                if (existing.isPresent()) {
//                    existing.get().getRoles().add(adminRole);
//                    userRepo.save(existing.get());
//                    log.info("ADMIN role assigned to existing user: admin");
//                    return;
//                }
//                userRepo.save(User.builder()
//                        .username("admin")
//                        .password(passwordEncoder.encode("admin"))
//                        .fullName("ADMIN-MANAGEMENT")
//                        .isEnabled(true)
//                        .roles(new HashSet<>(Set.of(adminRole)))
//                        .build());
//                log.info("admin account created with default credentials (admin/admin), please change it!");
//            } else {
//                log.info("Admin account already exists");
//            }
//        };
//    }
//}
