package com.phananh.e_commerce.application.service;

import com.phananh.e_commerce.presentation.dto.request.cart.CartAddItemRequest;
import com.phananh.e_commerce.presentation.dto.request.cart.CartUpdateItemRequest;
import com.phananh.e_commerce.presentation.dto.response.cart.CartItemResponse;

import java.util.List;

public interface CartItemService {
    List<CartItemResponse> getMyCart();

    void addProductToCart(CartAddItemRequest cartAddItemRequest);

    void removeProductFromCart(List<Long> cartItemId);

    CartItemResponse updateCartItem(CartUpdateItemRequest cartUpdateItemRequest);
}

