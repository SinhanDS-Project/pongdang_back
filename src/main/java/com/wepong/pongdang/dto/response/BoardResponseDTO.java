package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.BoardEntity;
import com.wepong.pongdang.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

public class BoardResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BoardListDTO {
        /** 한 페이지에 담긴 게시글 목록 */
        private Page<BoardDetailDTO> boards;
        /** 현재 페이지 번호 (1부터 시작) */
        private int currentPage;
        /** 페이지당 게시글 수 */
        private int pageSize;
        /** 전체 게시글 건수 */
        private long totalCount;
        /** 전체 페이지 수 */
        private int totalPages;
        /** (선택) 요청한 카테고리 */
        private Category category;

        public static BoardListDTO from(Page<BoardDetailDTO> boards, Category category) {
            return BoardListDTO.builder()
                    .boards(boards)
                    .currentPage(boards.getNumber() + 1)
                    .pageSize(boards.getSize())
                    .totalCount(boards.getTotalElements())
                    .totalPages(boards.getTotalPages())
                    .category(category)
                    .build();
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BoardDetailDTO {
        private String uid;
        private String title;
        private String content;
        private Category category;
        private int viewCount;
        private int likeCount;
        private String userUid;
        private String nickname;

        public static BoardDetailDTO from(BoardEntity boardEntity) {
            return BoardDetailDTO.builder()
                    .uid(boardEntity.getUid())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .category(boardEntity.getCategory())
                    .viewCount(boardEntity.getViewCount())
                    .likeCount(boardEntity.getLikeCount())
                    .userUid(boardEntity.getUserEntity().getUid())
                    .nickname(boardEntity.getUserEntity().getNickname())
                    .build();
        }
    }
}
