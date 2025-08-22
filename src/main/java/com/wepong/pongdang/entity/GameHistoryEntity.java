package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import com.wepong.pongdang.entity.enums.GameResult;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "game_history")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GameHistoryEntity extends BaseEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Column(nullable = false)
    private int bettingAmount;

    @Enumerated(EnumType.STRING)
    private GameResult gameResult;

    @Column(nullable = false)
    private int pointValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_uid")
    private GameEntity gameEntity;
}
