package com.wepong.pongdang.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurtlePlayerDTO {
    private String userUid;
    private String nickname;
    private String roomUid;
    private boolean isReady;
    private String turtleId;
    private int bettingPoint;
}
