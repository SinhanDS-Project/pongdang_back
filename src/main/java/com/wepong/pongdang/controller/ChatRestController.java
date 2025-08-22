package com.wepong.pongdang.controller;

import com.wepong.pongdang.dto.response.ChatQAResponseDTO;
import com.wepong.pongdang.entity.ChatBotQAEntity;
import com.wepong.pongdang.entity.enums.QAMainType;
import com.wepong.pongdang.entity.enums.QASubType;
import com.wepong.pongdang.service.ChatQAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

	@Autowired
	ChatQAService chatService;
		
	// ✅ 전체 Q&A 목록 반환
    @GetMapping("/allQuestion")
    public ChatQAResponseDTO.ChatQAListDTO getAllQuestions() {
        List<ChatBotQAEntity> list = chatService.selectAll();
        List<ChatQAResponseDTO.ChatQADetailDTO> details = list.stream()
                .map(ChatQAResponseDTO.ChatQADetailDTO::from)
                .collect(Collectors.toList());
        return ChatQAResponseDTO.ChatQAListDTO.builder().questions(details).build();
    }

    // ✅ 메인 카테고리에 따른 서브 카테고리 목록 반환
    @GetMapping(value = "/subcategories/{main_category}", produces = "application/json;charset=UTF-8")
    public List<QASubType> getSubCategoriesByMain(@PathVariable("main_category") QAMainType mainCategory) {
        return chatService.subCatesByMainCate(mainCategory);
    }

    // ✅ 메인+서브 카테고리에 따른 질문 목록 반환
    @GetMapping(value = "/questions/{main_category}/{sub_category}", produces = "application/json;charset=UTF-8")
    public ChatQAResponseDTO.ChatQAListDTO getQuestionsByMainAndSub(
    		@PathVariable("main_category") QAMainType mainCategory,
    		@PathVariable("sub_category") QASubType subCategory) {
        List<ChatBotQAEntity> list = chatService.selectByMainSubCate(mainCategory, subCategory);
        List<ChatQAResponseDTO.ChatQADetailDTO> details = list.stream()
                .map(ChatQAResponseDTO.ChatQADetailDTO::from)
                .collect(Collectors.toList());
        return ChatQAResponseDTO.ChatQAListDTO.builder().questions(details).build();
    }

    // ✅ UID로 답변만 조회
    @GetMapping(value = "/answer/{uid}", produces = "text/plain;charset=UTF-8")
    public String getAnswerByUid(@PathVariable("uid") Long uid) {
        return chatService.answerByUid(uid);
    }

    // ✅ 메인 카테고리로 질문 목록 조회 (서브 구분 없음)
    @GetMapping(value = "/questionsByMain/{main_category}", produces = "application/json;charset=UTF-8")
    public ChatQAResponseDTO.ChatQAListDTO getQuestionsByMainCategory(@PathVariable("main_category") QAMainType mainCategory) {
        List<ChatBotQAEntity> list = chatService.selectByMainCate(mainCategory);
        List<ChatQAResponseDTO.ChatQADetailDTO> details = list.stream()
                .map(ChatQAResponseDTO.ChatQADetailDTO::from)
                .collect(Collectors.toList());
        return ChatQAResponseDTO.ChatQAListDTO.builder().questions(details).build();
    }
	
}
