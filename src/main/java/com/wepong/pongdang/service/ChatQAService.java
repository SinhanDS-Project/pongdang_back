package com.wepong.pongdang.service;

import com.wepong.pongdang.entity.ChatBotQAEntity;
import com.wepong.pongdang.repository.ChatQARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatQAService {
	
	private final ChatQARepository chatQARepository;
	
	public List<ChatBotQAEntity> selectAll() {
		return chatQARepository.findAll();
	}
	
	public List<String> subCatesByMainCate(String main_category) {
		List<ChatBotQAEntity> chatList = chatQARepository.findByMainCategory(main_category);
		return chatList.stream()
				.map(ChatBotQAEntity::getSubCategory)
				.toList();
	}
		
	public List<ChatBotQAEntity> selectByMainSubCate(String main_category, String sub_category) {
		return chatQARepository.findByMainCategoryAndSubCategory(main_category, sub_category);
	}

	public String answerByUid(String uid) {
		ChatBotQAEntity chat = chatQARepository.findById(uid).orElseThrow(() -> new RuntimeException("질문이 존재하지 않습니다."));
		return chat.getAnswerText();
	}
	
	public List<ChatBotQAEntity> selectByMainCate(String main_category) {
		return chatQARepository.findByMainCategory(main_category);
	}
}
