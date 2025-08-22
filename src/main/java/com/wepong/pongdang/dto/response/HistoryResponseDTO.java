package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.GameHistoryEntity;
import com.wepong.pongdang.entity.PongHistoryEntity;
import com.wepong.pongdang.entity.enums.RankType;
import com.wepong.pongdang.entity.enums.PointHistoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class HistoryResponseDTO {
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GameResponseDTO {
		private Page<GameDetailResponseDTO> histories;
		private int currentPage;
		private int pageSize;
		private long totalCount;
		private int totalPages;

		public static GameResponseDTO from(Page<GameDetailResponseDTO> details) {
			return GameResponseDTO.builder()
					.histories(details)
					.currentPage(details.getNumber() + 1)
					.pageSize(details.getSize())
					.totalCount(details.getTotalElements())
					.totalPages(details.getTotalPages())
					.build();
		}
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GameDetailResponseDTO {
		private String uid;
		private String userUid;
		private String gameUid;
		private String gameName;
		private int bettingAmount;
		private RankType gameResult;
		private int pointValue;
		private LocalDateTime createdAt;

		public static GameDetailResponseDTO from(GameHistoryEntity history) {
			return GameDetailResponseDTO.builder()
					.uid(history.getUid())
					.userUid(history.getUserEntity().getUid())
					.gameUid(history.getGameEntity().getUid())
					.gameName(history.getGameEntity().getName())
					.bettingAmount(history.getBettingAmount())
					.gameResult(history.getGameResult())
					.pointValue(history.getPointValue())
					.createdAt(history.getCreatedAt())
					.build();
		}
	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PointResponseDTO {
		private Page<PointDetailResponseDTO> histories;
		private int currentPage;
		private int pageSize;
		private long totalCount;
		private int totalPages;

		public static PointResponseDTO from(Page<PointDetailResponseDTO> details) {
			return PointResponseDTO.builder()
					.histories(details)
					.currentPage(details.getNumber() + 1)
					.pageSize(details.getSize())
					.totalCount(details.getTotalElements())
					.totalPages(details.getTotalPages())
					.build();
		}
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PointDetailResponseDTO {
		private String uid;
		private String userUid;
		private PointHistoryType type;
		private int amount;
		private int balanceAfter;
		private String ghUid;
		private LocalDateTime createdAt;
		private String gameName;

		public static PointDetailResponseDTO from(PongHistoryEntity history) {
			return PointDetailResponseDTO.builder()
					.uid(history.getUid())
					.userUid(history.getUserEntity().getUid())
					.type(history.getType())
					.amount(history.getAmount())
					.balanceAfter(history.getBalanceAfter())
					.ghUid(history.getGameHistoryEntity().getUid())
					.createdAt(history.getCreatedAt())
					.gameName(history.getGameHistoryEntity().getGameEntity().getName())
					.build();
		}
	}
}
