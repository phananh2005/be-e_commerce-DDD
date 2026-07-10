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
        if (StringUtils.isBlank(fullName)) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }
        if (StringUtils.isBlank(phoneNumber)) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }

        this.fullName = fullName.trim();
        this.email = StringUtils.isBlank(email) ? null : email.trim();
        this.address = StringUtils.isBlank(address) ? null : address.trim();
        this.phoneNumber = phoneNumber.trim();
    }
}
