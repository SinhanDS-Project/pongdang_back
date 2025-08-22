package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.ChatBotQAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatQARepository extends JpaRepository<ChatBotQAEntity, Long> {
    List<ChatBotQAEntity> findByMainCategoryAndSubCategory(String mainCategory, String subCategory);

    List<ChatBotQAEntity> findByMainCategory(String mainCategory);
}
