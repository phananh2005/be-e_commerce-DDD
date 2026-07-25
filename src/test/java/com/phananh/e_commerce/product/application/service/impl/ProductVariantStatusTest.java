package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.order.domain.repository.OrderItemRepository;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import com.phananh.e_commerce.product.domain.model.enums.VariantStatus;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import com.phananh.e_commerce.product.presentation.dto.request.management.ProductUpdateRequest;
import com.phananh.e_commerce.product.presentation.dto.request.management.UpdateVariantStockQuantityAndPriceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductVariant Status Update Tests")
class ProductVariantStatusTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ManagementProductServiceImpl managementProductService;

    private Product product;
    private ProductVariant variant;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .build();

        variant = ProductVariant.builder()
                .id(100L)
                .product(product)
                .skuCode("SKU001")
                .price(BigDecimal.valueOf(100))
                .stockQuantity(50)
                .status(VariantStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Should update variant status via updateProduct")
    void testUpdateVariantStatusViaUpdateProduct() {
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setProductId(1L);
        request.setName("Updated Product");

        ProductUpdateRequest.VariantUpdateRequest variantRequest = new ProductUpdateRequest.VariantUpdateRequest();
        variantRequest.setVariantId(100L);
        variantRequest.setPrice(BigDecimal.valueOf(100));
        variantRequest.setStockQuantity(50);
        variantRequest.setStatus("INACTIVE");
        request.setExistVariants(java.util.Arrays.asList(variantRequest));

        when(productRepository.getProductById(1L)).thenReturn(Optional.of(product));
        when(productRepository.getVariantById(100L)).thenReturn(Optional.of(variant));

        assertDoesNotThrow(() -> {
            managementProductService.updateProduct(request);
        });

        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should update variant status via updateVariantStockQuantityAndPrice")
    void testUpdateVariantStatusViaUpdateStockAndPrice() {
        UpdateVariantStockQuantityAndPriceRequest request = new UpdateVariantStockQuantityAndPriceRequest();
        request.setStockQuantity(60);
        request.setPrice(BigDecimal.valueOf(120));
        request.setStatus("INACTIVE");

        when(productRepository.getVariantById(100L)).thenReturn(Optional.of(variant));

        assertDoesNotThrow(() -> {
            managementProductService.updateVariantStockQuantityAndPrice(100L, request);
        });

        verify(productRepository).save(any(ProductVariant.class));
    }

    @Test
    @DisplayName("Should throw INVALID_REQUEST for invalid status value")
    void testUpdateVariantWithInvalidStatus() {
        UpdateVariantStockQuantityAndPriceRequest request = new UpdateVariantStockQuantityAndPriceRequest();
        request.setStockQuantity(60);
        request.setPrice(BigDecimal.valueOf(120));
        request.setStatus("INVALID_STATUS");

        when(productRepository.getVariantById(100L)).thenReturn(Optional.of(variant));

        AppException exception = assertThrows(AppException.class, () -> {
            managementProductService.updateVariantStockQuantityAndPrice(100L, request);
        });

        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    @DisplayName("Should preserve status when not provided in request")
    void testPreserveStatusWhenNotProvided() {
        UpdateVariantStockQuantityAndPriceRequest request = new UpdateVariantStockQuantityAndPriceRequest();
        request.setStockQuantity(60);
        request.setPrice(BigDecimal.valueOf(120));
        request.setStatus(null);

        ProductVariant activeVariant = ProductVariant.builder()
                .id(100L)
                .product(product)
                .skuCode("SKU001")
                .price(BigDecimal.valueOf(100))
                .stockQuantity(50)
                .status(VariantStatus.ACTIVE)
                .build();

        when(productRepository.getVariantById(100L)).thenReturn(Optional.of(activeVariant));

        assertDoesNotThrow(() -> {
            managementProductService.updateVariantStockQuantityAndPrice(100L, request);
        });

        assertEquals(VariantStatus.ACTIVE, activeVariant.getStatus());
    }

    @Test
    @DisplayName("Should accept both ACTIVE and INACTIVE status")
    void testAcceptBothStatusValues() {
        ProductVariant inactiveVariant = ProductVariant.builder()
                .id(100L)
                .product(product)
                .skuCode("SKU001")
                .price(BigDecimal.valueOf(100))
                .stockQuantity(50)
                .status(VariantStatus.INACTIVE)
                .build();

        UpdateVariantStockQuantityAndPriceRequest requestActive = new UpdateVariantStockQuantityAndPriceRequest();
        requestActive.setStockQuantity(50);
        requestActive.setPrice(BigDecimal.valueOf(100));
        requestActive.setStatus("ACTIVE");

        when(productRepository.getVariantById(100L)).thenReturn(Optional.of(inactiveVariant));

        assertDoesNotThrow(() -> {
            managementProductService.updateVariantStockQuantityAndPrice(100L, requestActive);
        });

        assertEquals(VariantStatus.ACTIVE, inactiveVariant.getStatus());
    }
}
