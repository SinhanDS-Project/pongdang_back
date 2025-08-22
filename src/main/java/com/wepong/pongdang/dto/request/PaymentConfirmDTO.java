package com.wepong.pongdang.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 실제 토스페이먼츠에 결제 요청하는 dto
public class PaymentConfirmDTO {
    private String paymentKey;
    private String orderUid;
    private int amount;
}
