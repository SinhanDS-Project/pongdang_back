package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.GameRoomEntity;
import com.wepong.pongdang.entity.enums.GameRoomStatus;
import com.wepong.pongdang.entity.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class GameRoomResponseDTO {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GameRoomListDTO {
		private Page<GameRoomDetailDTO> gameRooms;
		private int currentPage;
		private int totalPages;

		public static GameRoomListDTO from(Page<GameRoomDetailDTO> gameRooms) {
			return GameRoomListDTO.builder()
					.gameRooms(gameRooms)
					.currentPage(gameRooms.getNumber() + 1)
					.totalPages(gameRooms.getTotalPages())
					.build();
		}
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GameRoomDetailDTO {
		private String uid;
		private String title;
		private int minBet;
		private GameRoomStatus status;
		private LocalDateTime createdAt;
		private LocalDateTime startAt;
		private String hostUid;
		private Level level;
		private String gameName;
		private int count;

		public static GameRoomDetailDTO from(GameRoomEntity room, int count) {
			return GameRoomDetailDTO.builder()
					.uid(room.getUid())
					.title(room.getTitle())
					.level(room.getGameLevelEntity().getLevel())
					.gameName(room.getGameLevelEntity().getGameEntity().getName())
					.count(count)
					.minBet(room.getMinBet())
					.hostUid(room.getUserEntity().getUid())
					.createdAt(room.getCreatedAt())
					.status(room.getStatus())
					.build();
		}
	}
}
