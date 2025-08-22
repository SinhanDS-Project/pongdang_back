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
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Column(nullable = false, columnDefinition = "VARCHAR(225)")
    private String title;

    @Column(nullable = false)
    private int minBet;

    @Enumerated(EnumType.STRING)
    private GameRoomStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_level_uid")
    private GameLevelEntity gameLevelEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_uid")
    private UserEntity userEntity;

    @Column
    private Date startedAt;

    public void updateStatus(GameRoomStatus status) {
        this.status = status;
    }

    public void updateHost(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
