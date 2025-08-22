package com.wepong.pongdang.service;

import com.wepong.pongdang.dto.request.ChatLogRequestDTO;
import com.wepong.pongdang.entity.ChatLogsEntity;
import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.repository.ChatLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatLogService {

	private final ChatLogRepository chatLogRepository;
	private final AuthService authService;

	public List<ChatLogsEntity> selectByUser(String user_uid) {
		return chatLogRepository.findByUserUid(user_uid);
	}
	
	public Page<ChatLogsEntity> selectByUser(String user_uid, int page) {
		int size = 10;
        int offset = (page-1) * size;
		Pageable pageable = PageRequest.of(offset / size, size);
		return chatLogRepository.findByUserUid(user_uid, pageable);
	}
	
	public int chatlogCount(String userId) {
		return chatLogRepository.countByUserUid(userId);
	}	
	
	public ChatLogsEntity selectByUid(String uid) {
		return chatLogRepository.findById(uid).orElseThrow(() -> new RuntimeException("채팅이 존재하지 않습니다."));
	}
	
	public ChatLogsEntity insertChatLog(ChatLogRequestDTO requestChatlog, String userId) {
		String uid = UUID.randomUUID().toString().replace("-", "");
		UserEntity userEntity = authService.findByUid(userId);

		ChatLogsEntity chatLog = ChatLogsEntity.builder()
				.uid(uid)
				.title(requestChatlog.getTitle())
				.question(requestChatlog.getQuestion())
				.userEntity(userEntity)
				.build();

		return chatLogRepository.save(chatLog);
	}
	
	public void deleteLog(String uid) {
		ChatLogsEntity chat = chatLogRepository.findById(uid).orElseThrow(() -> new RuntimeException("채팅이 존재하지 않습니다."));
		chatLogRepository.delete(chat);
	}

}
