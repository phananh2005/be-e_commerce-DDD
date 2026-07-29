package com.phananh.e_commerce.order.application.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementOrderResponse {
    @JsonIgnore
    @Deprecated
    private Long orderId;
    @JsonIgnore
    @Deprecated
    private Long userId;
    private String orderUuid;
    private String userUuid;
    private String username;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Item> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        @JsonIgnore
        @Deprecated
        private Long productId;
        private String productUuid;
        private String productName;
        private String skuCode;
        private Integer quantity;
        private BigDecimal price;
        private String variantImageUrl;
    }
}

