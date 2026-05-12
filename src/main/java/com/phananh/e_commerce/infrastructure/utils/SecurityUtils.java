package com.phananh.e_commerce.infrastructure.utils;

import com.phananh.e_commerce.domain.model.entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    public static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user found");
        }

        String currentUser = authentication.getName();
        if (currentUser == null || currentUser.isBlank()) {
            throw new IllegalStateException("Authenticated principal name is empty");
        }

        return currentUser;
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        // Case 1: principal Ä‘Ã£ lÃ  UserEntity
        if (principal instanceof User user) {
            return user;
        }

        // Case 2: principal lÃ  custom UserDetails (vÃ­ dá»¥ CustomUserDetails)
//         if (principal instanceof CustomUserDetails userDetails) {
//             return userDetails.getUser();
//         }

        throw new IllegalStateException("Authenticated principal is not a UserEntity");
    }
}


