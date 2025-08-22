package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.ChatBotQAEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ChatQAResponseDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatQAListDTO {
        private List<ChatQADetailDTO> questions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatQADetailDTO {
        private String uid;
        private String mainCategory;
        private String subCategory;
        private String questionText;
        private String answerText;

        public static ChatQADetailDTO from(ChatBotQAEntity chatBotQAEntity) {
            return ChatQADetailDTO.builder()
                    .uid(chatBotQAEntity.getUid())
                    .mainCategory(chatBotQAEntity.getMainCategory())
                    .subCategory(chatBotQAEntity.getSubCategory())
                    .questionText(chatBotQAEntity.getQuestionText())
                    .answerText(chatBotQAEntity.getAnswerText())
                    .build();
        }
    }
}
