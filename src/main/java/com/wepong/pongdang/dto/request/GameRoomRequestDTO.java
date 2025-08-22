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
		private int entryFee;
		private Long gameLevelId;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateGameRoomRequestDTO {
		private String title;
		private Long gameLevelId;
	}
}
