package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.ChatLogsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class ChatLogResponseDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatLogListDTO {
	    private Page<ChatLogDetailDTO> logs;
        private int total;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatLogDetailDTO {
        private String uid;
        private String title;
        private String question;
        private String response;
        private LocalDateTime chatDate;
        private LocalDateTime responseDate;
        private String userUid;
        private String nickname;

        public static ChatLogDetailDTO from(ChatLogsEntity log) {
            return ChatLogDetailDTO.builder()
                    .uid(log.getUid())
                    .title(log.getTitle())
                    .question(log.getQuestion())
                    .response(log.getResponse())
                    .chatDate(log.getChatDate())
                    .responseDate(log.getResponseDate())
                    .userUid(log.getUserEntity().getUid())
                    .nickname(log.getUserEntity().getNickname())
                    .build();
        }
    }
}
