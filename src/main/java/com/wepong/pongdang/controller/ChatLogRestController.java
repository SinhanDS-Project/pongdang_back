package com.wepong.pongdang.controller;

import com.wepong.pongdang.dto.request.ChatLogRequestDTO;
import com.wepong.pongdang.dto.response.ChatLogResponseDTO;
import com.wepong.pongdang.entity.ChatLogsEntity;
import com.wepong.pongdang.service.AuthService;
import com.wepong.pongdang.service.ChatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/chatlog")
public class ChatLogRestController {

	@Autowired
	ChatLogService chatLogService;
	@Autowired
	AuthService authService;
	
	// ✅ 사용자 UID로 채팅 로그 전체 조회
    @GetMapping("")
    public ChatLogResponseDTO.ChatLogListDTO getLogsByUserWithPaging(
										@RequestHeader("Authorization") String authHeader,
										@RequestParam(defaultValue = "1") int page) {
		Long userId = authService.validateAndGetUserId(authHeader);
    	Page<ChatLogsEntity> list = chatLogService.selectByUser(userId, page);
    	int totalCount = chatLogService.chatlogCount(userId);
		Page<ChatLogResponseDTO.ChatLogDetailDTO> details = list.map(ChatLogResponseDTO.ChatLogDetailDTO::from);
    	return ChatLogResponseDTO.ChatLogListDTO.builder()
    			.logs(details)
    			.total(totalCount)
    			.build();
    }
    
    // ✅ UID로 채팅 로그 상세 조회
    @GetMapping("/detail/{chatlog_uid}")
    public ChatLogResponseDTO.ChatLogDetailDTO getLogByUid(@PathVariable Long chatlogId) {
        ChatLogsEntity chatLogsEntity = chatLogService.selectByUid(chatlogId);
		return ChatLogResponseDTO.ChatLogDetailDTO.from(chatLogsEntity);
    }

    // ✅ 채팅 로그 등록
    @PostMapping(value = "/insertChatlog", produces = "text/plain;charset=utf-8", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertChatLog(@RequestBody ChatLogRequestDTO chatlog,
										@RequestHeader("Authorization") String authHeader) throws IOException {
		Long userId = authService.validateAndGetUserId(authHeader);
		
        chatLogService.insertChatLog(chatlog, userId);

		return ResponseEntity.ok("질문이 등록되었습니다.");
    }

    // ✅ 로그 삭제
    @DeleteMapping("/deleteChatlog/{chatlog_uid}")
    public ResponseEntity<?> deleteLog(@PathVariable Long chatlogId) {
        chatLogService.deleteLog(chatlogId);

		return ResponseEntity.ok("질문이 삭제되었습니다.");
    }
}
