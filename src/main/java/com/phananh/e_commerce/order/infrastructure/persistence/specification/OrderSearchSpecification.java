package com.phananh.e_commerce.order.infrastructure.persistence.specification;

import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class OrderSearchSpecification {

    public static Specification<Order> hasOrderCode(String orderCode) {
        return (root, query, cb) -> {
            if (orderCode == null || orderCode.isBlank()) return cb.conjunction();
            try {
                return cb.equal(root.get("id"), Long.parseLong(orderCode.trim()));
            } catch (NumberFormatException e) {
                return cb.equal(root.get("id"), -1L);
            }
        };
    }

    public static Specification<Order> hasFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("fullName")), "%" + fullName.trim().toLowerCase() + "%");
        };
    }

    public static Specification<Order> hasPhoneNumber(String phoneNumber) {
        return (root, query, cb) -> {
            if (phoneNumber == null || phoneNumber.isBlank()) return cb.conjunction();
            return cb.like(root.get("phoneNumber"), "%" + phoneNumber.trim() + "%");
        };
    }

    public static Specification<Order> hasShippingAddress(String shippingAddress) {
        return (root, query, cb) -> {
            if (shippingAddress == null || shippingAddress.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("shippingAddress")), "%" + shippingAddress.trim().toLowerCase() + "%");
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
}
