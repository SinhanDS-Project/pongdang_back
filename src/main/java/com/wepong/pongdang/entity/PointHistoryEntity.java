package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import com.wepong.pongdang.entity.enums.PointHistoryType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "point_history")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointHistoryEntity extends BaseEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Enumerated(EnumType.STRING)
    private PointHistoryType type;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int balanceAfter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gh_uid")
    private GameHistoryEntity gameHistoryEntity;
}
