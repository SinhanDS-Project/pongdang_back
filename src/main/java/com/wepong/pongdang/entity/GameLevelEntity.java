package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.enums.Level;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "game_level")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GameLevelEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(nullable = false)
    private double probability;

    @Column(nullable = false)
    private double reward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_uid")
    private GameEntity gameEntity;
}
