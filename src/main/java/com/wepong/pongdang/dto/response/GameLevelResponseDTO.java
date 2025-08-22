package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.GameLevelEntity;
import com.wepong.pongdang.entity.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class GameLevelResponseDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LevelListDTO {
        private List<LevelDetailDTO> levels;

        public static LevelListDTO from(List<LevelDetailDTO> levels) {
            return LevelListDTO.builder()
                    .levels(levels)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LevelDetailDTO {
        private String uid;
        private Level level;
        private double probability;
        private double reward;
        private String gameUid;

        public static LevelDetailDTO from(GameLevelEntity level) {
            return LevelDetailDTO.builder()
                    .uid(level.getUid())
                    .level(level.getLevel())
                    .reward(level.getReward())
                    .probability(level.getProbability())
                    .gameUid(level.getGameEntity().getUid())
                    .build();
        }
    }
}
