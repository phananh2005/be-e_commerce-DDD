package com.phananh.e_commerce.usermanagement.application.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserSortFieldMapperTest {

    @Test
    void mapsFullNameToEmbeddedUserInfoProperty() {
        assertEquals("info.fullName", UserSortFieldMapper.map("fullName"));
    }

    @Test
    void mapsUsernameToEmbeddedCredentialsProperty() {
        assertEquals("credentials.username", UserSortFieldMapper.map("username"));
    }
}
