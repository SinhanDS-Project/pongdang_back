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
        private Long id;
        private String title;
        private String question;
        private String response;
        private LocalDateTime chatDate;
        private LocalDateTime responseDate;
        private Long userId;
        private String nickname;

        public static ChatLogDetailDTO from(ChatLogsEntity log) {
            return ChatLogDetailDTO.builder()
                    .id(log.getId())
                    .title(log.getTitle())
                    .question(log.getQuestion())
                    .response(log.getResponse())
                    .chatDate(log.getChatDate())
                    .responseDate(log.getResponseDate())
                    .userId(log.getUser().getId())
                    .nickname(log.getUser().getNickname())
                    .build();
        }
    }
}
