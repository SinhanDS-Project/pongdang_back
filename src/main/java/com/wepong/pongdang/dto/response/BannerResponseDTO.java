package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.BannerEntity;
import com.wepong.pongdang.model.aws.S3ImagePathDeserializer;
import com.wepong.pongdang.model.aws.S3ImageUrlSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class BannerResponseDTO {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BannerListDTO {
        private List<BannerDetailDTO> banners;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BannerDetailDTO {
        private String uid;
        private String title;
        @JsonSerialize(using = S3ImageUrlSerializer.class)
        @JsonDeserialize(using = S3ImagePathDeserializer.class)
        private String imagePath;
        private String bannerLinkUrl;
        private String description;

        public static BannerDetailDTO from(BannerEntity bannerEntity) {
            return BannerDetailDTO.builder()
                    .uid(bannerEntity.getUid())
                    .title(bannerEntity.getTitle())
                    .imagePath(bannerEntity.getImagePath())
                    .bannerLinkUrl(bannerEntity.getBannerLinkUrl())
                    .description(bannerEntity.getDescription())
                    .build();
        }
    }
}
