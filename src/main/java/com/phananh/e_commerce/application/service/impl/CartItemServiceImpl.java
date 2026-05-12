package com.phananh.e_commerce.application.service.impl;

import com.phananh.e_commerce.presentation.dto.request.cart.CartAddItemRequest;
import com.phananh.e_commerce.presentation.dto.request.cart.CartUpdateItemRequest;
import com.phananh.e_commerce.presentation.dto.response.cart.CartItemResponse;
import com.phananh.e_commerce.application.exception.AppException;
import com.phananh.e_commerce.application.exception.ErrorCode;
import com.phananh.e_commerce.presentation.mapper.CartItemMapper;
import com.phananh.e_commerce.domain.model.entity.CartItem;
import com.phananh.e_commerce.domain.model.entity.ProductVariant;
import com.phananh.e_commerce.infrastructure.persistence.repository.CartItemRepository;
import com.phananh.e_commerce.infrastructure.persistence.repository.ProductVariantRepository;
import com.phananh.e_commerce.infrastructure.persistence.repository.UserRepository;
import com.phananh.e_commerce.application.service.CartItemService;
import com.phananh.e_commerce.application.service.ProductService;
import com.phananh.e_commerce.infrastructure.utils.SecurityUtils;
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

    ProductService productService;
    ProductVariantRepository productVariantRepository;
    UserRepository userRepository;
    CartItemMapper cartItemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getMyCart() {
        List<CartItem> cartItems = cartItemRepository.findByUser_Username(SecurityUtils.getCurrentUserName())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        return cartItems.stream()
                .map(cartItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void addProductToCart(CartAddItemRequest cartAddItemRequest) {
        ProductVariant variant = productVariantRepository.findById(cartAddItemRequest.getVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        if (variant.getStockQuantity() == 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }
        if(variant.getStockQuantity() < cartAddItemRequest.getQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        CartItem cartItem = CartItem.builder()
                .quantity(cartAddItemRequest.getQuantity())
                .variant(variant)
                .user(userRepository.findByUsername(SecurityUtils.getCurrentUserName())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)))
                .build();
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void removeProductFromCart(List<Long> cartItemId) {
        cartItemRepository.deleteByIdIn(cartItemId);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(CartUpdateItemRequest cartUpdateItemRequest) {
        CartItem cartItem = cartItemRepository.findById(cartUpdateItemRequest.getCartItemId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        ProductVariant variant = productVariantRepository.findById(cartUpdateItemRequest.getVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        if (variant.getStockQuantity() == 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        //trÆ°á»ng há»£p thay Ä‘á»•i sang sáº£n pháº©m Ä‘Ã£ cÃ³ trong giÃ² hÃ ng
        Optional<CartItem> existingCartItem = cartItemRepository
                .findByVariant_IdAndUser_Username(cartUpdateItemRequest.getVariantId(), SecurityUtils.getCurrentUserName());
        if (existingCartItem.isPresent()) {
            cartItemRepository.delete(cartItem);

            Integer updateQuantity = existingCartItem.get().getQuantity() + cartUpdateItemRequest.getQuantity();
            if(updateQuantity > variant.getStockQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }

            existingCartItem.get().setQuantity(updateQuantity);
            CartItem updateCartItem = cartItemRepository.save(existingCartItem.get());

            //            response.setVariants(productVariantRepository
//                    .findByProduct_Id(response.getProductId())
//                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND))
//                    .stream()
//                    .map(cartItemMapper::toVariantResponse)
//                    .toList());
            return cartItemMapper.toResponse(updateCartItem);
        }

        //trÆ°á»ng há»£p thay Ä‘á»•i sang sáº£n pháº©m chÆ°a cÃ³ trong giÃ² hÃ ng
        if(cartUpdateItemRequest.getQuantity() > variant.getStockQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        cartItem.setVariant(variant);
        cartItem.setQuantity(cartUpdateItemRequest.getQuantity());

        CartItem updateCartItem = cartItemRepository.save(cartItem);
        CartItemResponse response = cartItemMapper.toResponse(updateCartItem);
        response.setVariants(productVariantRepository
                .findByProduct_Id(response.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND))
                .stream()
                .map(cartItemMapper::toVariantResponse)
                .toList());
        return response;
    }
}

