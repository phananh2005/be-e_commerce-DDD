package com.phananh.e_commerce.usermanagement.infrastructure.persistence.specification;

import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class UserSearchSpecification {

    public static Specification<User> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            // navigate into embedded value objects: credentials.username and info.email/fullName
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("credentials").get("username")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("info").get("email")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("info").get("fullName")), likePattern)
            );
        };
    }

    public static Specification<User> hasRoleName(Set<RoleName> roleNames) {
        return (root, query, criteriaBuilder) -> {
            if (roleNames == null || roleNames.isEmpty()) return criteriaBuilder.conjunction();
            // avoid duplicate results when joining collection
            query.distinct(true);
            // join roles and check if role.name is in the given set of RoleName enums
            return root.join("roles").get("name").in(roleNames);
        };
    }
}
