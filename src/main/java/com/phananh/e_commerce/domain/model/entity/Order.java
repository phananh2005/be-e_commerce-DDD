package com.phananh.e_commerce.domain.model.entity;

import com.phananh.e_commerce.domain.model.enums.OrderStatus;
import com.phananh.e_commerce.domain.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "shipping_fee", precision = 19, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "shipping_address", columnDefinition = "TEXT", nullable = false)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 50)
    private PaymentMethod paymentMethod;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid = false; // Máº·c Ä‘á»‹nh lÃ  chÆ°a thanh toÃ¡n

    @Column(name = "payment_date")
    private LocalDateTime paymentDate; // LÆ°u thá»i Ä‘iá»ƒm tiá»n vá» tÃºi shop

//    @Column(name = "transaction_id", length = 100, unique = true)
//    private String transactionId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();
}

