package com.phananh.e_commerce.usermanagement.domain.model;

import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
public record UserInfo(
        @Column(name = "full_name", nullable = false)
        String fullName,
        @Column(unique = true, length = 100)
        String email,
        @Column(name = "address", columnDefinition = "TEXT")
        String address,
        @Column(name = "phone_number", unique = true, nullable = false)
        String phoneNumber) {

    @Builder
    public UserInfo(String fullName, String email, String address, String phoneNumber) {
        if (!StringUtils.isNotBlank(fullName)) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }
        if (!StringUtils.isNotBlank(phoneNumber)) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }

        this.fullName = fullName.trim();
        this.email = email.isBlank() ? null : email;
        this.address = address.trim();
        this.phoneNumber = phoneNumber.isBlank() ? null : phoneNumber;
    }
}
