package com.phananh.e_commerce.order.infrastructure.persistence.specification;

import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class OrderSearchSpecification {

    public static Specification<Order> hasOrderUuid(String orderUuid) {
        return (root, query, cb) -> {
            if (orderUuid == null) return cb.conjunction();
            return cb.equal(cb.lower(root.get("uuid")), orderUuid.toLowerCase());
        };
    }

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Order> hasCreatedFromDate(LocalDateTime createdFromDate) {
        return (root, query, cb) -> {
            if (createdFromDate == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("createdAt"), createdFromDate);
        };
    }

    public static Specification<Order> hasCreatedToDate(LocalDateTime createdToDate) {
        return (root, query, cb) -> {
            if (createdToDate == null) return cb.conjunction();
            return cb.lessThanOrEqualTo(root.get("createdAt"), createdToDate);
        };
    }

    public static Specification<Order> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return cb.conjunction();
            return cb.equal(root.get("userId"), userId);
        };
    }
}
