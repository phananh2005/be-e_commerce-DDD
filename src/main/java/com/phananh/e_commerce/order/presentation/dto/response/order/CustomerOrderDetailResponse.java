package com.phananh.e_commerce.order.presentation.dto.response.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CustomerOrderDetailResponse {

    private Long orderId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private String fullName;
    private boolean isPaid;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String phoneNumber;
    private String shippingAddress;
    private BigDecimal shippingFee;
    private String status;
    private BigDecimal totalPrice;
    private List<Item> items;

    @Data
    @Builder
    public static class Item {
        private Long productId;
        private String productName;
        private String skuCode;
        private Integer quantity;
        private BigDecimal price;
        private String variantImageUrl;
    }
}


