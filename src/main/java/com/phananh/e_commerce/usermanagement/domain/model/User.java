package com.phananh.e_commerce.usermanagement.domain.model;

import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserCredentials credentials;

    @Embedded
    private UserInfo info;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    private Set<Role> roles = new HashSet<>();

    public void updateInfo (UserInfo userInfo){
        this.info = userInfo;
    }

    public void changePassword (String oldPassword, String newPassword, PasswordEncoder passwordEncoder){
        if(passwordEncoder == null) throw new IllegalArgumentException("PasswordEncoder is required");
        this.credentials = this.credentials.changePassword(oldPassword, newPassword, passwordEncoder);
    }

    public void updateRoles(Set<Role> roles){
        this.roles.clear();
        this.roles = roles;
    }
}


