package com.phananh.e_commerce.order.application.service;

import com.phananh.e_commerce.order.presentation.dto.request.cart.CartAddItemRequest;
import com.phananh.e_commerce.order.presentation.dto.request.cart.CartUpdateItemRequest;
import com.phananh.e_commerce.order.presentation.dto.response.cart.CartItemResponse;
import com.phananh.e_commerce.order.domain.model.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemService {
    Optional<CartItem> addToCart(Long userId, Long variantId, Integer quantity);

    Optional<List<CartItem>> getCartItems(String username);

    void removeCartItem(Long cartItemId);

    void removeCartItems(List<Long> cartItemIds);

    CartItem updateCartItemQuantity(Long cartItemId, Integer quantity);

    List<CartItemResponse> getMyCart();

    void addProductToCart(CartAddItemRequest cartAddItemRequest);

    void removeProductFromCart(List<Long> cartItemIds);

    CartItemResponse updateCartItem(CartUpdateItemRequest cartUpdateItemRequest);
}


