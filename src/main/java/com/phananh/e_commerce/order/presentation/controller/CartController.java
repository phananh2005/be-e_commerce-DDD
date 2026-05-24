package com.phananh.e_commerce.order.presentation.controller;

import com.phananh.e_commerce.order.presentation.dto.request.cart.CartAddItemRequest;
import com.phananh.e_commerce.order.presentation.dto.request.cart.CartUpdateItemRequest;
import com.phananh.e_commerce.order.presentation.dto.response.cart.CartItemResponse;
import com.phananh.e_commerce.order.application.service.CartItemService;
import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-item")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Mục giỏ hàng", description = "Các API quản lý từng sản phẩm trong giỏ hàng của người dùng")
public class CartController {

    CartItemService cartItemService;

    @GetMapping("/my-cart")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getMyCart() {
        List<CartItemResponse> cartItems = cartItemService.getMyCart();
        if (cartItems == null)
            return ResponseEntity.ok(ApiResponse.<List<CartItemResponse>>builder()
                    .message("Cart is empty")
                    .build());

        return ResponseEntity.ok(ApiResponse.<List<CartItemResponse>>builder()
                .message("Get cart successfully")
                .result(cartItems)
                .build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductToCart(@RequestBody @Valid CartAddItemRequest cartAddItemRequest) {
        cartItemService.addProductToCart(cartAddItemRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove/{ids}")
    public ResponseEntity<ApiResponse<Void>> removeProductFromCart(@PathVariable List<Long> ids) {
        cartItemService.removeProductFromCart(ids);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody @Valid CartUpdateItemRequest cartUpdateItemRequest) {
        String message = cartItemService.updateCartItem(cartUpdateItemRequest);
        return ResponseEntity.ok(message);
    }
}
