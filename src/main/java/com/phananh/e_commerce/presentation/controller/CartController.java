package com.phananh.e_commerce.presentation.controller;

import com.phananh.e_commerce.presentation.dto.request.cart.CartAddItemRequest;
import com.phananh.e_commerce.presentation.dto.request.cart.CartUpdateItemRequest;
import com.phananh.e_commerce.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.presentation.dto.response.cart.CartItemResponse;
import com.phananh.e_commerce.application.service.CartItemService;
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
@Tag(name = "Má»¥c giá» hÃ ng", description = "CÃ¡c API quáº£n lÃ½ tá»«ng sáº£n pháº©m trong giá» hÃ ng cá»§a ngÆ°á»i dÃ¹ng")
public class CartController {

    CartItemService cartItemService;

    @GetMapping("/my-cart")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getMyCart() {
        List<CartItemResponse> cartItems = cartItemService.getMyCart();
        ApiResponse<List<CartItemResponse>> response = ApiResponse.<List<CartItemResponse>>builder()
                .message("Get cart successfully")
                .result(cartItems)
                .build();
        return ResponseEntity.ok(response);
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

