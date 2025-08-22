package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.GameLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameLevelRepository extends JpaRepository<GameLevelEntity, String> {
    List<GameLevelEntity> findByGameUid(String gameUid);
}
