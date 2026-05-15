package com.phananh.e_commerce.order.application.service.impl;

import com.phananh.e_commerce.order.domain.model.entity.CartItem;
import com.phananh.e_commerce.order.infrastructure.persistence.repository.CartItemRepository;
import com.phananh.e_commerce.order.application.service.CartItemService;
import com.phananh.e_commerce.order.presentation.dto.request.cart.CartAddItemRequest;
import com.phananh.e_commerce.order.presentation.dto.request.cart.CartUpdateItemRequest;
import com.phananh.e_commerce.order.presentation.dto.response.cart.CartItemResponse;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata.ProductVariantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemServiceImpl implements CartItemService {

    CartItemRepository cartItemRepository;
    ProductVariantRepository productVariantRepository;
    SpringDataUserRepository springDataUserRepository;

    @Override
    @Transactional
    public Optional<CartItem> addToCart(Long userId, Long variantId, Integer quantity) {
        if (quantity <= 0) {
            throw new AppException(ErrorCode.INVALID_QUANTITY);
        }

        var user = springDataUserRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

        if (variant.getStockQuantity() < quantity) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        CartItem cartItem = CartItem.builder()
                .user(user)
                .variant(variant)
                .quantity(quantity)
                .build();

        return Optional.of(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<CartItem>> getCartItems(String username) {
        return cartItemRepository.findByUser_Username(username);
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void removeCartItems(List<Long> cartItemIds) {
        cartItemRepository.deleteByIdIn(cartItemIds);
    }

    @Override
    @Transactional
    public CartItem updateCartItemQuantity(Long cartItemId, Integer quantity) {
        if (quantity <= 0) {
            throw new AppException(ErrorCode.INVALID_QUANTITY);
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (cartItem.getVariant().getStockQuantity() < quantity) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getMyCart() {
        // TODO: Implement get my cart logic
        return List.of();
    }

    @Override
    @Transactional
    public void addProductToCart(CartAddItemRequest cartAddItemRequest) {
        // TODO: Implement add product to cart logic
    }

    @Override
    @Transactional
    public void removeProductFromCart(List<Long> cartItemIds) {
        // TODO: Implement remove product from cart logic
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(CartUpdateItemRequest cartUpdateItemRequest) {
        // TODO: Implement update cart item logic
        return null;
    }
}


