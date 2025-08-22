package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.ChatBotQAEntity;
import com.wepong.pongdang.entity.enums.QAMainType;
import com.wepong.pongdang.entity.enums.QASubType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatQARepository extends JpaRepository<ChatBotQAEntity, Long> {
    List<ChatBotQAEntity> findByMainCategoryAndSubCategory(QAMainType mainCategory, QASubType subCategory);

    List<ChatBotQAEntity> findByMainCategory(QAMainType mainCategory);
}
