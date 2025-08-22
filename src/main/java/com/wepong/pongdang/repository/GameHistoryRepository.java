package com.wepong.pongdang.repository;

import com.wepong.pongdang.dto.response.HistoryResponseDTO;
import com.wepong.pongdang.entity.GameHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameHistoryRepository extends JpaRepository<GameHistoryEntity, Long> {
    HistoryResponseDTO.GameResponseDTO findByUserUid(String userId);

    int countByUserUid(String userId);

    Page<GameHistoryEntity> findByUserUid(String userId, Pageable pageable);
}
