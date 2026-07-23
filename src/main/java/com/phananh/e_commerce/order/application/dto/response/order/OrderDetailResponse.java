package com.phananh.e_commerce.order.application.dto.response.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDetailResponse {
    private Long orderId;
    private Long userId;
    private String orderUuid;
    private String userUuid;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private AddressInfo addressInfo;
    private Boolean isPaid;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private BigDecimal shippingFee;
    private String status;
    private BigDecimal totalPrice;
    private String cancellationReason;
    private List<Item> items;

    @Data
    @Builder
    public static class AddressInfo {
        private String fullName;
        private String phoneNumber;
        private String shippingAddress;
    }

    @Data
    @Builder
    public static class Item {
        private Long productId;
        private String productUuid;
        private String productName;
        private String skuCode;
        private Integer quantity;
        private BigDecimal price;
        private String variantImageUrl;
    }
}


