package com.wepong.pongdang.dto.request;

import com.wepong.pongdang.entity.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BoardRequestDTO {
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class InsertBoardRequestDTO {
		private String title;
		private String content;
	    private BoardType category;
	    private String boardImg;
		
	}
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class UpdateBoardRequestDTO {
		private String content;
	    private BoardType category;
	    private String title;
		
	}
}
