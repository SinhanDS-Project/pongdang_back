package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.GameEntity;
import com.wepong.pongdang.entity.enums.GameStatus;
import com.wepong.pongdang.entity.enums.GameType;
import com.wepong.pongdang.model.aws.S3ImagePathDeserializer;
import com.wepong.pongdang.model.aws.S3ImageUrlSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class GameResponseDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GameListDTO {
        private List<GameDetailDTO> games;

        public static GameListDTO from(List<GameDetailDTO> games) {
            return GameListDTO.builder()
                    .games(games)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GameDetailDTO {
        private String uid;
        private String name;
        private GameType type;
        private String description;

        @JsonSerialize(using = S3ImageUrlSerializer.class)
        @JsonDeserialize(using = S3ImagePathDeserializer.class)
        private String gameImg;

        private GameStatus status;
        private LocalDateTime createdAt;

        public static GameDetailDTO from(GameEntity gameEntity) {
            return GameDetailDTO.builder()
                    .uid(gameEntity.getUid())
                    .name(gameEntity.getName())
                    .type(gameEntity.getType())
                    .description(gameEntity.getDescription())
                    .gameImg(gameEntity.getGameImage())
                    .status(gameEntity.getStatus())
                    .createdAt(gameEntity.getCreatedAt())
                    .build();
        }
    }
}
