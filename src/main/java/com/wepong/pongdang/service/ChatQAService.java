package com.wepong.pongdang.service;

import com.wepong.pongdang.entity.ChatBotQAEntity;
import com.wepong.pongdang.entity.enums.QAMainType;
import com.wepong.pongdang.entity.enums.QASubType;
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
	
	public List<QASubType> subCatesByMainCate(QAMainType mainCategory) {
		List<ChatBotQAEntity> chatList = chatQARepository.findByMainCategory(mainCategory);
		return chatList.stream()
				.map(ChatBotQAEntity::getSubCategory)
				.toList();
	}
		
	public List<ChatBotQAEntity> selectByMainSubCate(QAMainType mainCategory, QASubType subCategory) {
		return chatQARepository.findByMainCategoryAndSubCategory(mainCategory, subCategory);
	}

	public String answerByUid(Long id) {
		ChatBotQAEntity chat = chatQARepository.findById(id).orElseThrow(() -> new RuntimeException("질문이 존재하지 않습니다."));
		return chat.getAnswerText();
	}
	
	public List<ChatBotQAEntity> selectByMainCate(QAMainType mainCategory) {
		return chatQARepository.findByMainCategory(mainCategory);
	}
}
