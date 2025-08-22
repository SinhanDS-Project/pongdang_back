package com.wepong.pongdang.service;

import com.wepong.pongdang.dto.request.GameRoomRequestDTO;
import com.wepong.pongdang.dto.response.GameRoomResponseDTO;
import com.wepong.pongdang.entity.GameEntity;
import com.wepong.pongdang.entity.GameLevelEntity;
import com.wepong.pongdang.entity.GameRoomEntity;
import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.entity.enums.GameRoomStatus;
import com.wepong.pongdang.model.multi.turtle.PlayerDAO;
import com.wepong.pongdang.repository.GameRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GameRoomService {

	private final PlayerDAO playerDAO;
	private final GameRoomRepository gameRoomRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private GameService gameService;

	@Autowired
	private GameLevelService gameLevelService;

	public GameRoomResponseDTO.GameRoomListDTO selectAll(int page) {
		int size = 6;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<GameRoomEntity> roomlist = gameRoomRepository.findAll(pageRequest);

		Page<GameRoomResponseDTO.GameRoomDetailDTO> details = roomlist
				.map(room -> {
					GameLevelEntity level = gameLevelService.selectByLevelUid(room.getGameLevelEntity().getUid());
					GameEntity gameEntity = level != null ? gameService.selectById(level.getGameEntity().getUid()) : null;
					int count = playerDAO.getAll(room.getUid()) != null ? playerDAO.getAll(room.getUid()).size() : 0;

					return GameRoomResponseDTO.GameRoomDetailDTO.from(room, count);
				});

		return GameRoomResponseDTO.GameRoomListDTO.from(details);
	}

	public List<GameRoomEntity> selectAll() {
		return gameRoomRepository.findAll();
	}

	public GameRoomResponseDTO.GameRoomDetailDTO selectById(String roomId) {
		GameRoomEntity room = gameRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("해당 게임방이 존재하지 않습니다"));

		if (room != null) {
			GameLevelEntity level = gameLevelService.selectByLevelUid(room.getGameLevelEntity().getUid());
			if (level != null) {
				GameEntity gameEntity = gameService.selectById(level.getGameEntity().getUid());
				if (gameEntity != null) {
					int count = playerDAO.getAll(room.getUid()) != null
							? playerDAO.getAll(room.getUid()).size()
							: 0;

					return GameRoomResponseDTO.GameRoomDetailDTO.from(room, count);
				}
			}
		}
		return null;
	}

	public void insertRoom(GameRoomRequestDTO.InsertGameRoomRequestDTO roomRequest, String userId) {
		String uid = UUID.randomUUID().toString().replace("-", "");
		UserEntity userEntity = authService.findByUid(userId);
		GameLevelEntity level = gameLevelService.selectByLevelUid(roomRequest.getGameLevelUid());

		GameRoomEntity room = GameRoomEntity.builder()
				.uid(uid)
				.title(roomRequest.getTitle())
				.minBet(roomRequest.getMinBet())
				.userEntity(userEntity)
				.gameLevelEntity(level)
				.status(GameRoomStatus.WAITING)
				.build();

		gameRoomRepository.save(room);
	}

//	public void updateRoom(GameRoomRequestDTO.UpdateGameRoomRequestDTO roomRequest, String userId, String roomId) {
//		GameRoom room = gameRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("해당 게임방이 존재하지 않습니다"));
//		// 게임방 존재 여부 && 현재 유저와 방장이 같은지 확인
//		if(room != null && room.getUser().getUid().equals(userId)) {
//			gameRoomRepository.save(room);
//		}
//	}

	public void deleteRoom(String roomId) {
		GameRoomEntity room = gameRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("해당 게임방이 존재하지 않습니다"));
		// 게임방 존재 여부
		if (room != null) {
			gameRoomRepository.delete(room);
		}
	}

	public void updateStatus(String roomId, GameRoomStatus status) {
		GameRoomEntity room = gameRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("해당 게임방이 존재하지 않습니다"));
		room.updateStatus(status);

		gameRoomRepository.save(room);
	}

	public void updateHost(String roomId, String hostId) {
		GameRoomEntity room = gameRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("해당 게임방이 존재하지 않습니다"));
		UserEntity userEntity = authService.findByUid(hostId);

		room.updateHost(userEntity);

		gameRoomRepository.save(room);
	}
}
