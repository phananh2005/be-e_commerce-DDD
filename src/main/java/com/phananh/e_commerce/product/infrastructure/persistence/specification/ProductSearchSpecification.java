package com.phananh.e_commerce.product.infrastructure.persistence.specification;

import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSearchSpecification {

    public static Specification<Product> hasNameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasBrandId(Long brandId) {
        return (root, query, criteriaBuilder) -> brandId == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("brandId"), brandId);
    }

    public static Specification<Product> hasBrandIds(List<Long> brandIds) {
        return (root, query, criteriaBuilder) -> brandIds == null || brandIds.isEmpty() ? criteriaBuilder.conjunction()
                : root.get("brandId").in(brandIds);
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> categoryId == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("categoryId"), categoryId);
    }

    public static Specification<Product> hasCategoryIds(List<Long> categoryIds) {
        return (root, query, criteriaBuilder) -> categoryIds == null || categoryIds.isEmpty()
                ? criteriaBuilder.conjunction()
                : root.get("categoryId").in(categoryIds);
    }

    public static Specification<Product> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Product> hasProductSearch(String productSearch) {
        return (root, query, criteriaBuilder) -> {
            if (productSearch == null || productSearch.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + productSearch.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("uuid")), productSearch.toLowerCase()),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));
        };
    }

    public static Specification<Product> hasStatus(ProductStatus status) {
        return (root, query, criteriaBuilder) -> status == null ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("status"), status);
    }
}
