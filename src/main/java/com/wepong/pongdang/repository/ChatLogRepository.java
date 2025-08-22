package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.ChatLogsEntity;
import com.wepong.pongdang.entity.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLogsEntity, Long> {
    int countByUserId(Long userId);

    List<ChatLogsEntity> findByUserId(Long userId);

    Page<ChatLogsEntity> findByUserId(Long userId, Pageable pageable);

    Long user(UserEntity user);
}
