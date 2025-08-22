package com.wepong.pongdang.service;

import com.wepong.pongdang.config.TossPaymentConfig;
import com.wepong.pongdang.dto.request.PaymentConfirmDTO;
import com.wepong.pongdang.entity.PaymentEntity;
import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.entity.enums.PaymentStatus;
import com.wepong.pongdang.repository.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TossPaymentConfig tossPaymentConfig;

    @Autowired
    private AuthService authService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PaymentEntity confirmPayment(PaymentConfirmDTO response, String userId) throws Exception {
//        PaymentDTO payment = paymentDAO.selectByOrderId(response.getOrder_uid());
//
//        if(payment == null) {
//            throw new IllegalArgumentException("존재하지 않는 주문번호입니다.");
//        }
//        if(!PaymentStatus.PENDING.equals(payment.getStatus())) {
//            throw new IllegalArgumentException("이미 처리된 결제입니다.");
//        }
//        if(payment.getAmount() != response.getAmount()) {
//            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
//        }

        String auth = Base64.getEncoder().encodeToString((tossPaymentConfig.getTossSecretKey()+":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + auth);

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", response.getPaymentKey());
        body.put("orderId", response.getOrderUid());
        body.put("amount", response.getAmount());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        PaymentStatus status = PaymentStatus.FAILED;
        String failure_reason = null;
        String receipt_url = null;
        Date approve_at = null;

        UserEntity userEntity = authService.findByUid(userId);

        PaymentEntity.PaymentEntityBuilder builder = PaymentEntity.builder()
                .uid(UUID.randomUUID().toString().replace("-", ""))
                .paymentKey(response.getPaymentKey())
                .orderUid(response.getOrderUid())
                .amount(response.getAmount())
                .userEntity(userEntity);

        try {
            ResponseEntity<String> confirm = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/confirm", request, String.class
            );

            if(confirm.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(confirm.getBody());
                String paymentKey = root.path("paymentKey").asText();
                String orderId = root.path("orderId").asText();
                String orderName = root.path("orderName").asText();
                String payType = root.path("method").asText();
                int amount = root.path("totalAmount").asInt();
                receipt_url = root.path("receipt").path("url").asText(null);
                String approvedAtStr = root.path("approvedAt").asText(null);
                if (approvedAtStr != null) {
                    approve_at = (Date) Date.from(ZonedDateTime.parse(approvedAtStr).toInstant());
                }
                status = PaymentStatus.PAID;

                builder.paymentKey(paymentKey)
                        .orderUid(orderId)
                        .orderName(orderName)
                        .payType(payType)
                        .amount(amount)
                        .approveAt(approve_at)
                        .receiptUrl(receipt_url);
            } else {
                failure_reason = "HTTP " + confirm.getStatusCodeValue();
            }
        } catch (HttpClientErrorException ex) {
            JsonNode error = objectMapper.readTree(ex.getResponseBodyAsString());
            failure_reason = error.path("code").asText() + ": " + error.path("message").asText();
        }

        builder.status(status)
                .failureReason(failure_reason);

        PaymentEntity paymentEntity = builder.build();

        return paymentRepository.save(paymentEntity);
    }
}
