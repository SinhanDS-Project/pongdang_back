package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import com.wepong.pongdang.entity.enums.GameStatus;
import com.wepong.pongdang.entity.enums.GameType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "game")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GameEntity extends BaseEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Column(nullable = false, columnDefinition = "VARCHAR(225)")
    private String name;

    @Enumerated(EnumType.STRING)
    private GameType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "VARCHAR(225) DEFAULT ''")
    private String gameImage;

    @Enumerated(EnumType.STRING)
    private GameStatus status;
}
