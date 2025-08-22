package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.ChatBotQAEntity;
import com.wepong.pongdang.entity.enums.QAMainType;
import com.wepong.pongdang.entity.enums.QASubType;

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
        private Long id;
        private QAMainType mainCategory;
        private QASubType subCategory;
        private String questionText;
        private String answerText;

        public static ChatQADetailDTO from(ChatBotQAEntity chatBotQAEntity) {
            return ChatQADetailDTO.builder()
                    .id(chatBotQAEntity.getId())
                    .mainCategory(chatBotQAEntity.getMainCategory())
                    .subCategory(chatBotQAEntity.getSubCategory())
                    .questionText(chatBotQAEntity.getQuestionText())
                    .answerText(chatBotQAEntity.getAnswerText())
                    .build();
        }
    }
}
