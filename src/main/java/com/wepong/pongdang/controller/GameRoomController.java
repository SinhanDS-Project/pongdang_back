package com.wepong.pongdang.controller;

import com.wepong.pongdang.dto.response.ChatResponseDTO;
import com.wepong.pongdang.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GameRoomController {

    private final SimpMessagingTemplate messagingTemplate;
    private final AuthService authService;

    // 채팅
    @MessageMapping("/{roomId}/chat")
    public void handleGameAction(@DestinationVariable Long roomId, ChatResponseDTO chat, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        String nickname = authService.findById(userId).getNickname();
        chat.setSender(nickname);

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, chat);
    }
}
