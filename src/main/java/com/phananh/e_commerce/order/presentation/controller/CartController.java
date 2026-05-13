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
        return ResponseEntity.ok(ApiResponse.<List<CartItemResponse>>builder()
                .message("Get cart successfully")
                .result(cartItems)
                .build());
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addProductToCart(@RequestBody @Valid CartAddItemRequest cartAddItemRequest) {
        cartItemService.addProductToCart(cartAddItemRequest);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Add product to cart successfully")
                .build());
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> removeProductFromCart(@PathVariable List<Long> cartItemId) {
        cartItemService.removeProductFromCart(cartItemId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Remove product from cart successfully")
                .build());
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItem(@RequestBody @Valid CartUpdateItemRequest cartUpdateItemRequest) {
        CartItemResponse cartItemResponse = cartItemService.updateCartItem(cartUpdateItemRequest);
        return ResponseEntity.ok(ApiResponse.<CartItemResponse>builder()
                .message("Update cart item successfully")
                .result(cartItemResponse)
                .build());
    }
}
