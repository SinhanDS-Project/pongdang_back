package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import com.wepong.pongdang.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity(name = "payment")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(20)")
    private String payType;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String orderUid;

    @Column(columnDefinition = "VARCHAR(255)")
    private String orderName;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String paymentKey;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column
    private Date approveAt;

    @Column
    private String failureReason;

    @Column
    private String receiptUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
