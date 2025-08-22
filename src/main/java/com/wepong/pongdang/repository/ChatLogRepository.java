package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.ChatLogsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLogsEntity, String> {
    int countByUserUid(String userId);

    List<ChatLogsEntity> findByUserUid(String userUid);

    Page<ChatLogsEntity> findByUserUid(String userUid, Pageable pageable);
}
