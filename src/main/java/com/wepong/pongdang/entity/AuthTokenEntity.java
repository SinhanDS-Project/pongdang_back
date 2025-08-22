package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "auth_token")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthTokenEntity extends BaseEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Column(nullable = false, columnDefinition = "VARCHAR(200)")
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private UserEntity userEntity;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
