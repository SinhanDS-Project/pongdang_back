package com.wepong.pongdang.controller;

import com.wepong.pongdang.dto.response.BoardResponseDTO;
import com.wepong.pongdang.entity.BoardEntity;
import com.wepong.pongdang.entity.enums.BoardType;
import com.wepong.pongdang.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/support")
public class SupportRestController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list/{category}")
    public Object getBoardsByCategory(@PathVariable BoardType category,
                                      @RequestParam(required = false, defaultValue = "1") int page) {

        if (category.equals(BoardType.NOTICE)) {
            int pageSize = 10;
            Page<BoardEntity> boards = boardService.getBoards(page - 1, pageSize, category);
            Page<BoardResponseDTO.BoardDetailDTO> details = boards.map(BoardResponseDTO.BoardDetailDTO::from);
            return BoardResponseDTO.BoardListDTO.from(details, category);
        } else {
            // FAQ: 페이징 X, 정렬 X
            List<BoardEntity> boardEntities = boardService.getBoards(category);
            return boardEntities.stream()
                    .map(BoardResponseDTO.BoardDetailDTO::from)
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/detail/{boardId}")
    public BoardResponseDTO.BoardDetailDTO getBoardDetail(@PathVariable Long boardId) {
        BoardEntity boardEntity = boardService.getBoardByUid(boardId);
        return BoardResponseDTO.BoardDetailDTO.from(boardEntity);
    }
}
