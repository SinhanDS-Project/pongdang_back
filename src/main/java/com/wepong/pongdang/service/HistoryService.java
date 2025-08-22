package com.wepong.pongdang.service;

import com.wepong.pongdang.dto.response.HistoryResponseDTO;
import com.wepong.pongdang.entity.GameHistoryEntity;
import com.wepong.pongdang.entity.PointHistoryEntity;
import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.entity.enums.PointHistoryType;
import com.wepong.pongdang.repository.GameHistoryRepository;
import com.wepong.pongdang.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoryService {

    private final GameHistoryRepository gameHistoryRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Autowired
    private AuthService authService;

    public HistoryResponseDTO.GameResponseDTO gameHistoryList(String userId) {
        return gameHistoryRepository.findByUserUid(userId);
    }

    public HistoryResponseDTO.PointResponseDTO pointHistoryList(String userId) {
        return pointHistoryRepository.findByUserUid(userId);
    }
    
    public int gameHistoryCount(String userId) {
      return gameHistoryRepository.countByUserUid(userId);
    }

    public int pointHistoryCount(String userId) {
      return pointHistoryRepository.countByUserUid(userId);
    }

    
    public HistoryResponseDTO.GameResponseDTO gameHistoryList(String userId, int page) {
        int size = 10;
        int offset = (page - 1) * size;
        Pageable pageable = PageRequest.of(offset / size, size);
        Page<GameHistoryEntity> list = gameHistoryRepository.findByUserUid(userId, pageable);

        Page<HistoryResponseDTO.GameDetailResponseDTO> details = list.map(HistoryResponseDTO.GameDetailResponseDTO::from);

        return HistoryResponseDTO.GameResponseDTO.from(details);
    }

    public HistoryResponseDTO.PointResponseDTO pointHistoryList(String userId, int page) {
        int size = 10;
        int offset = (page - 1) * size;
        Pageable pageable = PageRequest.of(offset / size, size);
        Page<PointHistoryEntity> list = pointHistoryRepository.findByUserUid(userId, pageable);

        Page<HistoryResponseDTO.PointDetailResponseDTO> details = list.map(HistoryResponseDTO.PointDetailResponseDTO::from);

        return HistoryResponseDTO.PointResponseDTO.from(details);
    }

    public void insertGameHistory(GameHistoryEntity gameHistoryEntity, String userId) {
        String uid = UUID.randomUUID().toString().replace("-", "");
        UserEntity userEntity = authService.findByUid(userId);
        GameHistoryEntity history = GameHistoryEntity.builder()
                .uid(uid)
                .userEntity(userEntity)
                .gameEntity(gameHistoryEntity.getGameEntity())
                .pointValue(gameHistoryEntity.getPointValue())
                .gameResult(gameHistoryEntity.getGameResult())
                .bettingAmount(gameHistoryEntity.getBettingAmount())
                .build();
        gameHistoryRepository.save(history);
    }

    public void insertPointHistory(PointHistoryEntity pointHistoryEntity, String userId) {
        String uid = UUID.randomUUID().toString().replace("-", "");
        UserEntity userEntity = authService.findByUid(userId);

        PointHistoryEntity history = PointHistoryEntity.builder()
                .uid(uid)
                .userEntity(userEntity)
                .type(pointHistoryEntity.getType())
                .balanceAfter(pointHistoryEntity.getBalanceAfter())
                .amount(pointHistoryEntity.getAmount())
                .gameHistoryEntity(pointHistoryEntity.getGameHistoryEntity())
                .build();

        pointHistoryRepository.save(history);
    }

    public void insertPointHistory(String userId, int amount) {
        String uid = UUID.randomUUID().toString().replace("-", "");
        UserEntity userEntity = authService.findByUid(userId);

        PointHistoryEntity history = PointHistoryEntity.builder()
                .uid(uid)
                .userEntity(userEntity)
                .type(PointHistoryType.CHARGE)
                .balanceAfter(userEntity.getPointBalance() + amount)
                .amount(amount)
                .build();

        pointHistoryRepository.save(history);
    }
}
