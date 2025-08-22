package com.wepong.pongdang.repository;

import com.wepong.pongdang.dto.response.HistoryResponseDTO;
import com.wepong.pongdang.entity.PongHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PongHistoryEntity, String> {
    HistoryResponseDTO.PointResponseDTO findByUserUid(String userId);

    int countByUserUid(String userId);

    Page<PongHistoryEntity> findByUserUid(String userId, Pageable pageable);
}
