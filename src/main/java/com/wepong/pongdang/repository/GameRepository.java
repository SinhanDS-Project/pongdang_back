package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.GameEntity;
import com.wepong.pongdang.entity.enums.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, String> {
    List<GameEntity> findByType(GameType type);

    List<GameEntity> findByName(String name);
}
