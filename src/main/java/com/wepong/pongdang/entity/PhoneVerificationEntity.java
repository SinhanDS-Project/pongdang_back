package com.wepong.pongdang.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;

@Entity(name = "phone_verification")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PhoneVerificationEntity {

    @Id
    @Column(columnDefinition = "VARCHAR(100)")
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(6)")
    private String verificationCode;

    @Column(nullable = false)
    private Timestamp expiredAt;

    @Column(nullable = false)
    private boolean isVerified;

    public void markVerified() {
        this.isVerified = true;
    }
}
