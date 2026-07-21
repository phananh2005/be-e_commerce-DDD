package com.phananh.e_commerce.usermanagement.domain.model;

import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", columnDefinition = "VARCHAR(36)", unique = true, nullable = false)
    private String uuid;

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
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    public void updateInfo (UserInfo userInfo){
        this.info = userInfo;
    }

    public void changePassword (String oldPassword, String newPassword){
        this.credentials = this.credentials.changePassword(oldPassword, newPassword);
    }

    public void updateRoles(Set<Role> roles){
        this.roles.clear();
        this.roles = roles;
    }

    public void active(){this.credentials = this.credentials.activeUser();}

    public void inactive(){this.credentials = this.credentials.inactiveUser();}

    @PrePersist
    public void generateUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}


