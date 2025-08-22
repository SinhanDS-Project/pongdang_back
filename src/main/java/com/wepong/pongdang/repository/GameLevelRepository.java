package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.GameLevelEntity;
import com.wepong.pongdang.entity.enums.Level;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameLevelRepository extends JpaRepository<GameLevelEntity, Long> {
    List<GameLevelEntity> findByGameId(Long gameId);
}
