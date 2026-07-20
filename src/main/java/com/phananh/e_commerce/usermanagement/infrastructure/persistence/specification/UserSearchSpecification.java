package com.phananh.e_commerce.usermanagement.infrastructure.persistence.specification;

import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Set;

public class UserSearchSpecification {

    public static Specification<User> hasUserIdentifier(String userIdentifier) {
        return (root, query, criteriaBuilder) -> {
            if (userIdentifier == null || userIdentifier.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            try {
                Long userId = Long.parseLong(userIdentifier);
                return criteriaBuilder.equal(root.get("id"), userId);
            } catch (NumberFormatException e) {
                return criteriaBuilder.equal(root.get("credentials").get("username"), userIdentifier);
            }
        };
    }

    public static Specification<User> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("info").get("fullName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("info").get("email")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("info").get("phoneNumber")), likePattern)
            );
        };
    }

    public static Specification<User> hasRoleName(Set<RoleName> roleNames) {
        return (root, query, criteriaBuilder) -> {
            if (roleNames == null || roleNames.isEmpty()) return criteriaBuilder.conjunction();
            query.distinct(true);
            return root.join("roles").get("name").in(roleNames);
        };
    }

    public static Specification<User> isEnabled(Boolean enabled) {
        return (root, query, criteriaBuilder) -> {
            if (enabled == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("credentials").get("isEnabled"), enabled);
        };
    }

    public static Specification<User> createdAtFrom(LocalDateTime createdDateFrom) {
        return (root, query, criteriaBuilder) -> {
            if (createdDateFrom == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdDateFrom);
        };
    }

    public static Specification<User> createdAtTo(LocalDateTime createdDateTo) {
        return (root, query, criteriaBuilder) -> {
            if (createdDateTo == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdDateTo);
        };
    }
}
