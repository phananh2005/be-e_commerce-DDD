package com.phananh.e_commerce.usermanagement.application.service.impl;

public class UserSortFieldMapper {
    private static final String FULL_NAME = "fullName";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String ADDRESS = "address";
    private static final String ENABLED = "enabled";

    public static String map(String sortBy) {
        if (sortBy == null) {
            return "createdAt";
        }

        return switch (sortBy) {
            case FULL_NAME -> "info.fullName";
            case USERNAME -> "credentials.username";
            case EMAIL -> "info.email";
            case PHONE_NUMBER -> "info.phoneNumber";
            case ADDRESS -> "info.address";
            case ENABLED -> "credentials.isEnabled";
            default -> sortBy;
        };
    }
}
