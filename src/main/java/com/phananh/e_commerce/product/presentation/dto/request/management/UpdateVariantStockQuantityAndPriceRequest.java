package com.phananh.e_commerce.product.presentation.dto.request.management;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateVariantStockQuantityAndPriceRequest {

    Integer stockQuantity;
    BigDecimal price;
}
