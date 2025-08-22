package com.wepong.pongdang.repository;

import com.wepong.pongdang.dto.response.HistoryResponseDTO;
import com.wepong.pongdang.entity.PongHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PongHistoryRepository extends JpaRepository<PongHistoryEntity, Long> {
    HistoryResponseDTO.PointResponseDTO findByUserId(Long userId);

    int countByUserId(Long userId);

    Page<PongHistoryEntity> findByUserId(Long userId, Pageable pageable);
}
