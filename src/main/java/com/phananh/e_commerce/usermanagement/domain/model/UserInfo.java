package com.phananh.e_commerce.usermanagement.domain.model;

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
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }

        this.fullName = fullName;
        this.email = email.isBlank() ? null : email;
        this.address = address;
        this.phoneNumber = phoneNumber.isBlank() ? null : phoneNumber;
    }
}
