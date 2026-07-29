package com.phananh.e_commerce.order.domain.repository;

import com.phananh.e_commerce.order.domain.model.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {
    List<CartItem> getByUserId(Long userId);

    Optional<CartItem> getByUserIdAndVariantId(Long userId, Long variantId);

    List<CartItem> getByListId(List<Long> ids);

    List<CartItem> getByListUuid(List<String> uuids);

    Optional<CartItem> getById(Long id);

    Optional<CartItem> getByUuid(String uuid);

    CartItem save(CartItem cartItem);

    void delete(CartItem cartItem);

    void deleteAll(Iterable<CartItem> cartItems);
}

