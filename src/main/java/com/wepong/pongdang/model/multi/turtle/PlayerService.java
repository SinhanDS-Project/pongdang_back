package com.wepong.pongdang.model.multi.turtle;

import com.wepong.pongdang.dto.response.TurtlePlayerDTO;
import com.wepong.pongdang.entity.GameRoomEntity;
import com.wepong.pongdang.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    PlayerDAO playerDAO;
    @Autowired
    GameRoomService gameRoomService;

    // 거북이 게임방 플레이어 상세 조회
    public List<TurtlePlayerDTO> getPlayers(Long roomId) {
        List<TurtlePlayerDTO> players = playerDAO.getAll(roomId);
        return players;
    }

    // 각 게임방 플레이어 수
    public Map<Long, Integer> getAllPlayers() {
        Map<Long, Integer> roomPlayers = new HashMap<>();
        List<Long> roomIds = gameRoomService.selectAll().stream()
                .map(GameRoomEntity::getId)
                .collect(Collectors.toList());
        for (Long roomId : roomIds) {
            List<TurtlePlayerDTO> players = playerDAO.getAll(roomId);
            // 플레이어가 없으면 0 처리
            int count = (players != null) ? players.size() : 0;
            roomPlayers.put(roomId, count);
        }
        return roomPlayers;
    }
}
