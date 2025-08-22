package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.PaymentEntity;
import com.wepong.pongdang.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private String payType;
    private int amount;
    private String orderUid;
    private String orderName;
    private PaymentStatus status;
    private Date approveAt;
    private String failureReason;
    private String receiptUrl;
    private Long userId;
    private String userName;

    public static PaymentResponseDTO from(PaymentEntity paymentEntity) {
        return PaymentResponseDTO.builder()
                .id(paymentEntity.getId())
                .payType(paymentEntity.getPayType())
                .amount(paymentEntity.getAmount())
                .orderUid(paymentEntity.getOrderUid())
                .orderName(paymentEntity.getOrderName())
                .status(paymentEntity.getStatus())
                .approveAt(paymentEntity.getApproveAt())
                .receiptUrl(paymentEntity.getReceiptUrl())
                .failureReason(paymentEntity.getFailureReason())
                .userId(paymentEntity.getUser().getId())
                .userName(paymentEntity.getUser().getUserName())
                .build();
    }
}
