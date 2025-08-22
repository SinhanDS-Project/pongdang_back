package com.wepong.pongdang.service;

import com.wepong.pongdang.entity.GameEntity;
import com.wepong.pongdang.entity.enums.GameType;
import com.wepong.pongdang.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GameService {

    private final GameRepository gameRepository;

    // 전체 조회
    public List<GameEntity> selectAll() {
        return gameRepository.findAll();
    }

    // 타입으로 조회
    public List<GameEntity> selectByType(GameType type) {
        return gameRepository.findByType(type);
    }

    // uid로 조회
    public GameEntity selectById(String gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("게임이 존재하지 않습니다."));
    }
    
    // 이름으로 조회
    public List<GameEntity> selectByName(String name) {
		  return gameRepository.findByName(name);
	  }
}
	 

