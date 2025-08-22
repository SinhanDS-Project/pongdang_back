package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import com.wepong.pongdang.entity.enums.GameRoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity(name = "game_room")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GameRoomEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(225)")
    private String title;

    @Enumerated(EnumType.STRING)
    private GameRoomStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_level_id")
    private GameLevelEntity gameLevel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private UserEntity user;

    public void updateStatus(GameRoomStatus status) {
        this.status = status;
    }

    public void updateHost(UserEntity user) {
        this.user = user;
    }
}
