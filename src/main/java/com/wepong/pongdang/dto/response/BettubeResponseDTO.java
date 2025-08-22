package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.BettubeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class BettubeResponseDTO {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BettubeListDTO {
        private List<BettubeDetailDTO> bettubes;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BettubeDetailDTO {
        private Long id;
        private String title;
        private String bettubeUrl;
        private String description;

        public static BettubeDetailDTO from(BettubeEntity bettubeEntity) {
            return BettubeDetailDTO.builder()
                    .id(bettubeEntity.getId())
                    .title(bettubeEntity.getTitle())
                    .bettubeUrl(bettubeEntity.getBettubeUrl())
                    .description(bettubeEntity.getDescription())
                    .build();
        }
    }
}
