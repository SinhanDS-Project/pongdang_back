package com.wepong.pongdang.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GameRoomRequestDTO {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InsertGameRoomRequestDTO {
		private String title;
		private int minBet;
		private String gameLevelUid;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateGameRoomRequestDTO {
		private String title;
		private String gameLevelUid;
	}
}
