package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import com.wepong.pongdang.entity.enums.Level;
import com.wepong.pongdang.entity.enums.RankType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "reward_per_result")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RewardPerResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RankType rank;

    @Column(nullable = false)
    private int reward;

    @Column(nullable = false)
    private int donation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_level_id")
    private GameLevelEntity gameLevel;
}
