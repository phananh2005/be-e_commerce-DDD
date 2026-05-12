package com.phananh.e_commerce.presentation.dto.response.oder;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderSummaryResponse {
    private Long orderId;
    private BigDecimal totalPrice;
    private String status;
    private List<Item> items;

    @Data
    @Builder
    public static class Item {
        private String productName;
        private String skuCode;
        private Integer quantity;
        private BigDecimal price;
        private String variantImageUrl;
    }
}

