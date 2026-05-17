package com.phananh.e_commerce.usermanagement.domain.model;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.PasswordUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
public record UserCredentials(
        @Column(nullable = false, unique = true, length = 50)
        String username,

        @Column(nullable = false)
        String password,

        @Column(nullable = false)
        Boolean enabled) {

    @Builder
    public UserCredentials(String username, String password, Boolean enabled) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (enabled == null) {
            throw new IllegalArgumentException("Enabled cannot be null");
        }
        this.username = username.trim();
        this.password = PasswordUtils.encode(password.trim());
        this.enabled = enabled;
    }

    public UserCredentials changePassword(String oldPassword, String newPassword) {
        boolean verifyPassword = PasswordUtils.matches(oldPassword, this.password);
        if (!verifyPassword) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        String newPasswordEncoded = PasswordUtils.encode(newPassword);

        return new UserCredentials(this.username, newPasswordEncoded, this.enabled);
    }
}
