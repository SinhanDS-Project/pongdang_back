package com.wepong.pongdang.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TossPaymentConfig {

    @Value("${toss.client-key}")
    private String tossClientKey;

    @Value("${toss.secret-key}")
    private String tossSecretKey;
}
