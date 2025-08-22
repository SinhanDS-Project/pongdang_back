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
    private Long userId;
    private String nickname;
    private Long roomId;
    private boolean isReady;
    private String turtleId;
    private int entryFee;
}
