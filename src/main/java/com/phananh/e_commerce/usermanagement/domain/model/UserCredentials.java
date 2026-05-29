package com.phananh.e_commerce.usermanagement.domain.model;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.PasswordUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserCredentials(
        @Column(nullable = false, unique = true, length = 50)
        String username,

        @Column(nullable = false)
        String password,

        @Column(nullable = false)
        Boolean isEnabled) {

    public UserCredentials(String username, String password, Boolean isEnabled) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (isEnabled == null) {
            throw new IllegalArgumentException("Enabled cannot be null");
        }
        this.username = username.trim();
        this.password = PasswordUtils.encode(password.trim());
        this.isEnabled = isEnabled;
    }

    public UserCredentials changePassword(String oldPassword, String newPassword) {
        boolean verifyPassword = PasswordUtils.matches(oldPassword, this.password);
        if (!verifyPassword) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        return new UserCredentials(this.username, newPassword, this.isEnabled);
    }

    public UserCredentials activeUser(){
        return new UserCredentials(this.username, this.password, true);
    }

    public UserCredentials inactiveUser(){
        return new UserCredentials(this.username, this.password, false);
    }
}
